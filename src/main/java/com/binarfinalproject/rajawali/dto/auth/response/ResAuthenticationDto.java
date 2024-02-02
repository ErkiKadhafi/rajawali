package com.binarfinalproject.rajawali.dto.auth.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
class ResAuthenticationRoleDto {
  private String name;
}

@Data
public class ResAuthenticationDto {
  private UUID id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String accessToken;

  private String type = "Bearer";

  private List<ResAuthenticationRoleDto> roles;
}
