package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.auth.request.EnableUserDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordSendOtpDto;
import com.binarfinalproject.rajawali.dto.auth.request.SigninDto;
import com.binarfinalproject.rajawali.dto.auth.request.SignupDto;
import com.binarfinalproject.rajawali.dto.auth.response.ResAuthenticationDto;
import com.binarfinalproject.rajawali.entity.Notification;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.entity.Notification.NotificationType;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.NotificationRepository;
import com.binarfinalproject.rajawali.repository.RoleRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.AuthenticationService;
import com.binarfinalproject.rajawali.service.JWTService;
import com.binarfinalproject.rajawali.util.EmailSender;
import com.binarfinalproject.rajawali.util.EmailTemplate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final int otpLength = 6;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailTemplate emailTemplate;

    @Autowired
    EmailSender emailSender;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String createOTP() {
        Random random = new Random();
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            int randomNumber = random.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return oneTimePassword.toString().trim();
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public Map<String, String> registerUser(SignupDto request) throws ApiException {
        if (!Objects.equals(request.getConfirmationPassword(), request.getPassword()))
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Password doesn't match! ");

        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());
        if (userOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Email  '" + request.getEmail() + "' is already exist.");

        boolean phoneNumberExist = userRepository.existByPhoneNumber(request.getPhoneNumber());
        if (phoneNumberExist)
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Phone number  '" + request.getPhoneNumber() + "' is already exist.");
        String userOtp = createOTP();

        User user = modelMapper.map(request, User.class);
        user.setOtp(userOtp);
        user.setOtpExpired(LocalDateTime.now().plusMinutes(5));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsEnabled(true);
        userRepository.save(user);

        // send email
        String templateEmail = emailTemplate.getRegisterTemplate(request.getFullName(), userOtp);
        emailSender.sendEmail(request.getEmail(), "Register new account", templateEmail);

        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Please check your email and enter the OTP!");
        return response;
    }

    @Override
    public ResAuthenticationDto enableUser(EnableUserDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());

        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User with email '" + request.getEmail() + "' doesn't exist");

        if (LocalDateTime.now().isAfter(userOnDb.get().getOtpExpired()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP is expired!");

        if (userOnDb.get().getOtpUsed())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP is used!");

        if (!userOnDb.get().getOtp().equals(request.getOtpCode()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP doesnt match!");

        User user = userOnDb.get();
        user.setOtpUsed(true);
        user.setIsEnabled(true);
        userRepository.save(user);

        ResAuthenticationDto resAuthenticationDto = modelMapper.map(user, ResAuthenticationDto.class);
        resAuthenticationDto.setAccessToken(jwtService.generateToken(user));
        return resAuthenticationDto;
    }

    @Override
    public ResAuthenticationDto authenticateUser(SigninDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());

        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User with email '" + request.getEmail() + "' doesn't exist");

        if (!userOnDb.get().isEnabled())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Your account hasn't activated yet");

        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(), request.getPassword()));
            User user = userOnDb.get();

            ResAuthenticationDto resAuthenticationDto = modelMapper.map(user, ResAuthenticationDto.class);
            resAuthenticationDto.setAccessToken(jwtService.generateToken(user));

            String dateFormatted = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());

            Notification notification = new Notification();
            notification.setUser(user);
            notification.setNotificationType(NotificationType.AUTHENTICATION);
            notification.setDescription(
                    "New login from another device. On " + dateFormatted + ". Not you? Please change the password");
            notificationRepository.save(notification);

            user.setNotificationIsSeen(false);
            userRepository.save(user);

            return resAuthenticationDto;
        } catch (AuthenticationException e) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Password doesn't match!");
        }
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public Map<String, String> forgotPasswordSendOtp(ForgotPasswordSendOtpDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Email  '" + request.getEmail() + "' is not found.");

        String userOtp = createOTP();
        User user = userOnDb.get();
        user.setOtp(userOtp);
        user.setOtpUsed(false);
        user.setOtpExpired(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        // send email
        String templateEmail = emailTemplate.getResetPassword(request.getEmail(), userOtp);
        emailSender.sendEmail(request.getEmail(), "Register new account", templateEmail);

        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Please check your email and enter the OTP!");
        return response;
    }

    @Override
    public Map<String, String> forgotPassword(ForgotPasswordDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Email  '" + request.getEmail() + "' is not found.");

        if (LocalDateTime.now().isAfter(userOnDb.get().getOtpExpired()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP is expired!");

        if (userOnDb.get().getOtpUsed())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP is used!");

        if (!userOnDb.get().getOtp().equals(request.getOtpCode()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP doesnt match!");

        User user = userOnDb.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtpUsed(true);
        userRepository.save(user);

        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Forgot password success, please sign in again!");
        return response;
    }
}
