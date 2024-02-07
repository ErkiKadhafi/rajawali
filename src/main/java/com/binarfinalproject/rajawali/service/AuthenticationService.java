package com.binarfinalproject.rajawali.service;

import java.util.Map;

import com.binarfinalproject.rajawali.dto.auth.request.EnableUserDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordDto;
import com.binarfinalproject.rajawali.dto.auth.request.ForgotPasswordSendOtpDto;
import com.binarfinalproject.rajawali.dto.auth.request.RefreshTokenDto;
import com.binarfinalproject.rajawali.dto.auth.request.SigninDto;
import com.binarfinalproject.rajawali.dto.auth.request.SignupDto;
import com.binarfinalproject.rajawali.dto.auth.response.ResAuthenticationDto;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface AuthenticationService {
    public Map<String, String> registerUser(SignupDto request) throws ApiException;

    public ResAuthenticationDto enableUser(EnableUserDto request) throws ApiException;

    public ResAuthenticationDto authenticateUser(SigninDto request) throws ApiException;

    public ResAuthenticationDto refreshToken(RefreshTokenDto request) throws ApiException;

    public Map<String, String> forgotPasswordSendOtp(ForgotPasswordSendOtpDto request) throws ApiException;

    public Map<String, String> forgotPassword(ForgotPasswordDto request) throws ApiException;
}
