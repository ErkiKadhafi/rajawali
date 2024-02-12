package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.auth.request.EnableUserDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordSendOtpDto;
import com.binarfinalproject.rajawali.dto.auth.request.RefreshTokenDto;
import com.binarfinalproject.rajawali.dto.auth.request.SigninDto;
import com.binarfinalproject.rajawali.dto.auth.request.SignupDto;
import com.binarfinalproject.rajawali.dto.auth.response.ResAuthenticationDto;
import com.binarfinalproject.rajawali.entity.Notification;
import com.binarfinalproject.rajawali.entity.RefreshToken;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.entity.Notification.NotificationType;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.NotificationRepository;
import com.binarfinalproject.rajawali.repository.RefreshTokenRepository;
import com.binarfinalproject.rajawali.repository.RoleRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.AuthenticationService;
import com.binarfinalproject.rajawali.service.JWTService;
import com.binarfinalproject.rajawali.util.EmailSender;
import com.binarfinalproject.rajawali.util.EmailTemplate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${admin.qa.email}")
    private String adminQAEmail;

    @Value("${admin.qa.password}")
    private String adminQAPassword;

    @Value("${admin.qa.otp}")
    private String adminQAOtp;

    private final int otpLength = 6;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

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

    private String createOTP() {
        Random random = new Random();
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            int randomNumber = random.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return oneTimePassword.toString().trim();
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000 * 3)); // 3 days
        refreshToken.setUser(user);

        return refreshTokenRepository.saveAndFlush(refreshToken);
    }

    private void createAccountQA() {
        Optional<User> userOnDb = userRepository.findByEmail(adminQAEmail);
        User user;
        if (userOnDb.isPresent()) {
            user = userOnDb.get();
            user.setOtpUsed(false);
            user.setIsEnabled(false);
            userRepository.save(user);

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(user.getId());
            if (refreshToken.isPresent())
                refreshTokenRepository.delete(refreshToken.get());

            return;
        }
        user = new User();
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN").get()));
        user.setFullName("Admin QA");
        user.setEmail(adminQAEmail);
        user.setPassword(passwordEncoder.encode(adminQAPassword));
        user.setPhoneNumber("081250000000");
        user.setOtp(adminQAOtp);
        user.setOtpExpired(LocalDateTime.now().plusYears(1));
        userRepository.save(user);
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public Map<String, String> registerUser(SignupDto request) throws ApiException {
        Map<String, String> response = new HashMap<String, String>();

        // only for qa
        if (request.getEmail().equals(adminQAEmail)) {
            createAccountQA();
            response.put("message", "Please check your email and enter the OTP!");
            return response;
        }

        if (!Objects.equals(request.getConfirmationPassword(), request.getPassword()))
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Password doesn't match! ");

        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());
        if (userOnDb.isPresent()) {
            User user = userOnDb.get();
            if (user.getIsEnabled())
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Your account is already exist and enabled, please login");

            // OTP not expired
            if (LocalDateTime.now().isBefore(user.getOtpExpired()))
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Email '" + request.getEmail()
                                + "' is already exist. Please check your email and enter the correct OTP!");

            String userOtp = createOTP();
            user.setOtp(userOtp);
            user.setOtpExpired(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);

            // send email
            String templateEmail = emailTemplate.getRegisterTemplate(request.getFullName(), userOtp);
            emailSender.sendEmail(request.getEmail(), "Register new account", templateEmail);
            response.put("message", "Success renewing your OTP, please check your email");
            return response;
        }

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
        user.setIsEnabled(false);
        userRepository.save(user);

        // send email
        String templateEmail = emailTemplate.getRegisterTemplate(request.getFullName(), userOtp);
        emailSender.sendEmail(request.getEmail(), "Register new account", templateEmail);

        response.put("message", "Please check your email and enter the OTP!");
        return response;
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResAuthenticationDto enableUser(EnableUserDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());

        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User with email '" + request.getEmail() + "' doesn't exist");

        if (userOnDb.get().isEnabled())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Your account is already enabled.");

        if (LocalDateTime.now().isAfter(userOnDb.get().getOtpExpired()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP is expired!");

        if (userOnDb.get().getOtpUsed())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "OTP is used!");

        if (!userOnDb.get().getOtp().equals(request.getOtpCode()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "OTP doesnt match!");

        User user = userOnDb.get();
        user.setOtpUsed(true);
        user.setIsEnabled(true);
        User updatedUser = userRepository.saveAndFlush(user);

        ResAuthenticationDto resAuthenticationDto = modelMapper.map(updatedUser, ResAuthenticationDto.class);
        resAuthenticationDto.setAccessToken(jwtService.generateToken(updatedUser));
        resAuthenticationDto.setRefreshToken(createRefreshToken(updatedUser).getToken());

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

            Optional<RefreshToken> refreshTokenOnDb = refreshTokenRepository.findByUserId(user.getId());
            RefreshToken refreshToken = refreshTokenOnDb.isEmpty() ? createRefreshToken(user) : refreshTokenOnDb.get();

            ResAuthenticationDto resAuthenticationDto = modelMapper.map(user, ResAuthenticationDto.class);
            resAuthenticationDto.setAccessToken(jwtService.generateToken(user));
            if (request.getRememberMe() != null && request.getRememberMe())
                resAuthenticationDto.setRefreshToken(refreshToken.getToken());

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
        user.setOtp(request.getEmail().equals(adminQAEmail) ? adminQAOtp : userOtp);
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
            throw new ApiException(HttpStatus.BAD_REQUEST,
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

    @Override
    public ResAuthenticationDto refreshToken(RefreshTokenDto request) throws ApiException {
        Optional<RefreshToken> refreshTokenOnDb = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (refreshTokenOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Refresh token '" + request.getRefreshToken() + "' is not found!. Please login again");

        RefreshToken refreshToken = refreshTokenOnDb.get();
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Refresh token '" + request.getRefreshToken() + "' is already expired");
        }

        refreshTokenRepository.delete(refreshToken);
        User user = refreshToken.getUser();
        ResAuthenticationDto resAuthenticationDto = modelMapper.map(user, ResAuthenticationDto.class);
        resAuthenticationDto.setRefreshToken(createRefreshToken(user).getToken());
        resAuthenticationDto.setAccessToken(jwtService.generateToken(user));

        return resAuthenticationDto;
    }
}
