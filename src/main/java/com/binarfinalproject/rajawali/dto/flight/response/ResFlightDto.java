package com.binarfinalproject.rajawali.dto.flight.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResFlightDto {
    private UUID id;
    private UUID sourceAirportId;
    private UUID destinationAirportId;
    private UUID airplaneId;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private double economySeatsPrice;
    private double businessSeatsPrice;
    private double firstSeatsPrice;
    private double discount;
}