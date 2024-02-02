package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.auth.request.EnableUserDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordSendOtpDto;
import com.binarfinalproject.rajawali.dto.auth.request.SigninDto;
import com.binarfinalproject.rajawali.dto.auth.request.SignupDto;
import com.binarfinalproject.rajawali.dto.auth.response.ResAuthenticationDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.AuthenticationService;
import com.binarfinalproject.rajawali.util.ResponseMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  @Autowired
  AuthenticationService authenticationService;

  @PostMapping("/sign-up")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupDto signUpRequestCreate) {
    try {
      Map<String, String> response = authenticationService.registerUser(signUpRequestCreate);

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Register success!",
          response);
    } catch (ApiException e) {
      return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
    } catch (Exception e) {
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @PostMapping("/enable-user")
  public ResponseEntity<Object> enableUser(@Valid @RequestBody EnableUserDto request) {
    try {
      ResAuthenticationDto response = authenticationService.enableUser(request);

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "User successfully enabled!",
          response);
    } catch (ApiException e) {
      return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
    } catch (Exception e) {
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<Object> authenticateUser(@Valid @RequestBody SigninDto request) {
    try {
      ResAuthenticationDto response = authenticationService.authenticateUser(request);

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
          "Login success!", response);
    } catch (ApiException e) {
      return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
    } catch (Exception e) {
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @PostMapping("/forgot-password/send-otp")
  public ResponseEntity<Object> forgotPasswordSendOtp(@Valid @RequestBody ForgotPasswordSendOtpDto request) {
    try {
      Map<String, String> response = authenticationService.forgotPasswordSendOtp(request);

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
          "Forgot password send OTP success!", response);
    } catch (ApiException e) {
      return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
    } catch (Exception e) {
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordDto request) {
    try {
      Map<String, String> response = authenticationService.forgotPassword(request);

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
          "Forgot password success!", response);
    } catch (ApiException e) {
      return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
    } catch (Exception e) {
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
