package com.binarfinalproject.rajawali.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SigninDto {
	@NotNull
	@Email
	private String email;

	@NotBlank
	private String password;
}
