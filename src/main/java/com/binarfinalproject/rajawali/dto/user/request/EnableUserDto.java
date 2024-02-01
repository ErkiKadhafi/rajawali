package com.binarfinalproject.rajawali.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnableUserDto {
    @NotBlank
    @Email
    private String emailAddress;

    @NotNull
    private Integer otpCode;

}
