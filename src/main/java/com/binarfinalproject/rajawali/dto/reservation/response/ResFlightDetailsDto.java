package com.binarfinalproject.rajawali.dto.reservation.response;

import java.util.List;

import lombok.Data;

@Data
public class ResFlightDetailsDto {
    private String id;

    private String flightId;

    private Boolean useTravelAssurance;

    private Boolean useBagageAssurance;

    private Boolean useFlightDelayAssurance;

    private Double seatPrice;

    private Double totalPrice;

    private List<ResPassengerDetailsDto> passengerDetailList;
}
