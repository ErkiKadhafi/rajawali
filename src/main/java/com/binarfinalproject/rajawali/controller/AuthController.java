package com.binarfinalproject.rajawali.controller;


import com.binarfinalproject.rajawali.dto.auth.request.LoginRequest;
import com.binarfinalproject.rajawali.dto.auth.request.SignupRequest;
import com.binarfinalproject.rajawali.dto.auth.response.JwtResponse;
import com.binarfinalproject.rajawali.dto.auth.response.MessageResponse;
import com.binarfinalproject.rajawali.dto.user.ResUserDto;
import com.binarfinalproject.rajawali.entity.Role;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.enums.ERole;
import com.binarfinalproject.rajawali.repository.RoleRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.security.jwt.JwtUtils;
import com.binarfinalproject.rajawali.security.service.SignUpService;
import com.binarfinalproject.rajawali.security.service.UserDetailsImpl;
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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private SignUpService signUpService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

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
  public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (signUpService.checkUsername(signUpRequest)){
      return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "User is already in use!");
    }

    if (signUpService.checkGmail(signUpRequest)){
      return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "gmail is already in use!");
    }

    try {
      ResUserDto response = signUpService.createAccount(signUpRequest);
      if (Objects.nonNull(response)){
        return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "User registered successfully!", response);
      } else {
        return ResponseMapper.generateResponseFailed(HttpStatus.BAD_REQUEST, "error");
      }
    } catch (Exception e){
      return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

  }
}
