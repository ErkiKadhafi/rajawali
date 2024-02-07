package com.binarfinalproject.rajawali.dto.user.response;

import java.util.UUID;

import lombok.Data;

@Data
public class ResProfileDto {
    private UUID id;

    private String fullName;

    private String phoneNumber;
}
