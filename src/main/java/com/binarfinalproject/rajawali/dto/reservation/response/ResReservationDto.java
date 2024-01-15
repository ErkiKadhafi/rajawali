package com.binarfinalproject.rajawali.dto.reservation.response;

import java.util.List;

import lombok.Data;

@Data
public class ResReservationDto {
    private String id;

    private String classType;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private Double totalPrice;

    private List<ResFlightDetailsDto> flightDetails;
}