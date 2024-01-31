package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.config.secuirty.JwtUtils;
import com.binarfinalproject.rajawali.dto.Roles.response.ResRolesDto;
import com.binarfinalproject.rajawali.dto.auth.request.CreateSignupRequest;
import com.binarfinalproject.rajawali.dto.auth.request.LoginRequest;
import com.binarfinalproject.rajawali.dto.auth.response.JwtResponse;
import com.binarfinalproject.rajawali.dto.user.ResUserDto;
import com.binarfinalproject.rajawali.dto.user.request.EnableUserDto;
import com.binarfinalproject.rajawali.entity.Role;
import com.binarfinalproject.rajawali.entity.Role.ERole;
import com.binarfinalproject.rajawali.entity.User;
//import com.binarfinalproject.rajawali.enums.ERole;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.RoleRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.AuthenticationService;
import com.binarfinalproject.rajawali.util.EmailSender;
import com.binarfinalproject.rajawali.util.EmailTemplate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthenticationService {
    private final Long expiryInterval = 5L * 60 * 1000;
    private final int otpLength = 6;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    public UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    @Transactional
    public ResUserDto register(CreateSignupRequest signupRequest) throws ApiException {
        try {
            if (userRepository.existsByEmail(signupRequest.getEmail()) || userRepository.existsByUsername(signupRequest.getUsername())){
                throw new ApiException(HttpStatus.BAD_REQUEST, "username / email" +
                        signupRequest.getEmail() + "is already exist!");
            }

            if (!Objects.equals(signupRequest.getConfirmation_password(), signupRequest.getPassword())){
                throw new ApiException(HttpStatus.BAD_REQUEST, "Password doesn't match! ");
            }
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            //generate otp and save
            Integer OTP = createRandomOneTimePassword();
            User user = new User(signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()),
                    signupRequest.getHandphone(),
                    roles,
                    OTP,
                    new Date(System.currentTimeMillis() + expiryInterval)
            );
            userRepository.save(user);

            //send email
            String templateEmail = emailTemplate.getRegisterTemplate(signupRequest.getUsername(), OTP);
            emailSender.createEmail(signupRequest.getEmail(), "Register new account", templateEmail);

            ResUserDto response = modelMapper.map(user, ResUserDto.class);
            response.setSet_roles(roles.stream().map(data -> modelMapper.map(data, ResRolesDto.class))
                    .collect(Collectors.toSet()));
            return response;
        } catch (Exception e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public ResUserDto regenerate(String email) throws ApiException{
        try{
            User user = userDetailsService.loadUserByemail(email);
            Integer OTP = createRandomOneTimePassword();
            user.setOneTimePasswordCode(OTP);
            user.setExpired(new Date(System.currentTimeMillis() + expiryInterval));
            userRepository.save(user);
            String templateEmail = emailTemplate.getRegisterTemplate(email, OTP);
            emailSender.createEmail(email, "Register new account", templateEmail);
            return modelMapper.map(user, ResUserDto.class);
        } catch (Exception e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Integer createRandomOneTimePassword() {
        Random random = new Random();
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            int randomNumber = random.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return Integer.parseInt(oneTimePassword.toString().trim());
    }

    @Override
    public Boolean otpIsValid(EnableUserDto request) {
        Boolean statusGenerated = false;
        User user = userDetailsService.loadUserByemail(request.getEmailAddress());
        Instant expiredInstant = user.getExpired().toInstant(); // Konversi java.util.Date ke java.time.Instant
        Instant now = Instant.now();
        if (user.getOneTimePasswordCode().equals(request.getOtpCode()) && Duration.between(expiredInstant, now).getSeconds() < (180)){
            user.setIsUsed(true);
            userRepository.save(user);
            statusGenerated = true;
        }
        return statusGenerated;
    }

}
