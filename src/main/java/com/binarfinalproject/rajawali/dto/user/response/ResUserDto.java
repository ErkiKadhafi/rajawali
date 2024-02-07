package com.binarfinalproject.rajawali.dto.user.response;

import java.util.List;

import lombok.Data;

@Data
class ResUserRoleDto {
    private String name;
}

@Data
public class ResUserDto {
    private String id;

    private List<ResUserRoleDto> roles;

    private String fullName;

    private String email;

    private String phoneNumber;

    private long reservationsMade;
}
