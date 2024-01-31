package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.auth.request.CreateSignupRequest;
import com.binarfinalproject.rajawali.dto.auth.request.LoginRequest;
import com.binarfinalproject.rajawali.dto.auth.response.JwtResponse;
import com.binarfinalproject.rajawali.dto.user.ResUserDto;
import com.binarfinalproject.rajawali.dto.user.request.EnableUserDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {
    public ResUserDto register(@NotNull CreateSignupRequest signupRequest) throws ApiException;
    public Integer createRandomOneTimePassword();

    public Boolean otpIsValid(EnableUserDto request);
    public ResUserDto regenerate(String email) throws ApiException;

}
