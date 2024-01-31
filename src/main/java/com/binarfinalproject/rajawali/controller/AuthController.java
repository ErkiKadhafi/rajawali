package com.binarfinalproject.rajawali.controller;


import com.binarfinalproject.rajawali.dto.auth.request.LoginRequest;
import com.binarfinalproject.rajawali.dto.auth.request.CreateSignupRequest;
import com.binarfinalproject.rajawali.dto.auth.response.JwtResponse;
import com.binarfinalproject.rajawali.dto.user.ResUserDto;
import com.binarfinalproject.rajawali.config.secuirty.JwtUtils;
import com.binarfinalproject.rajawali.dto.user.request.EnableUserDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordSendOtpDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.AuthenticationService;
import com.binarfinalproject.rajawali.service.impl.UserDetailsImpl;
import com.binarfinalproject.rajawali.service.impl.UserDetailsServiceImpl;
import com.binarfinalproject.rajawali.util.ResponseMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;


  @Autowired
  public UserDetailsServiceImpl userDetailsService;



  @PostMapping("/signin")
  public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());

      return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Login is successfully", new JwtResponse(
              jwt,
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              roles));
    } catch (Exception e){
      return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, e.getMessage());
    }

  }


  @PostMapping("/signup")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody CreateSignupRequest signUpRequestCreate) {

    try {
      ResUserDto response = authenticationService.register(signUpRequestCreate);
      Map<String, String> res = new HashMap<String, String>();
      if (Objects.nonNull(response)){
        res.put("message", "Please check your email and enter the OTP!");
        return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Register new account!", res);
      } else {
        return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "error");
      }
    } catch (Exception e){
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

  }



  @PostMapping("/enable-user")
  public ResponseEntity<Object> enableUser(@Valid @RequestBody EnableUserDto request){
    Boolean statusGenerated = authenticationService.otpIsValid(request);
    if (statusGenerated){
      return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Register Success", request.getEmailAddress());
    } else {
      return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "Token is invalid or already expired");
    }

  }

  @PutMapping("/regenerate-otp")
  public ResponseEntity<Object> regenerateOTP(@RequestBody ForgotPasswordSendOtpDto mail) throws ApiException{
    try {
      ResUserDto response = authenticationService.regenerate(mail.getEmailAddress());
      Map<String, String> res = new HashMap<String, String>();
        res.put("message", "Please check your email and enter the OTP!");
        return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "New Otp", response);
    } catch (Exception e){
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }


}
