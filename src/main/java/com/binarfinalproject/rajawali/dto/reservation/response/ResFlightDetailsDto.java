package com.binarfinalproject.rajawali.dto.reservation.response;

import java.util.List;

import lombok.Data;

@Data
public class ResFlightDetailsDto {
    private String id;

    private String flightId;

    private Boolean useAssurance;

    private Double seatPrice;

    private Double totalPrice;

    private List<ResPassengerDto> passengers;
}
