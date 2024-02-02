package com.binarfinalproject.rajawali.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnableUserDto {
    @NotBlank
    @Email
    private String email;

    @NotNull
    private String otpCode;

}
