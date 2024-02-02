package com.binarfinalproject.rajawali.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String otpCode;

    @NotBlank
    private String newPassword;
}
