package com.binarfinalproject.rajawali.dto.reservation.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResPassengerDto {
    private String id;

    private String seatId;

    private String genderType;

    private String ageType;

    private String fullname;

    private String idCardNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
