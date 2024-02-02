package com.binarfinalproject.rajawali.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupDto {
  @NotBlank
  private String fullName;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  @NotBlank
  @Size(min = 6, max = 40)
  private String confirmationPassword;
}
