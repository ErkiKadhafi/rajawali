package com.binarfinalproject.rajawali.dto.reservation.response;

import lombok.Data;

@Data
public class ResListReservationDto {
    private String id;

    private String classType;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private Double totalPrice;
}