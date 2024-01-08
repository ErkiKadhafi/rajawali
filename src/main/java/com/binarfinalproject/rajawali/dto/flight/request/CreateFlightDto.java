package com.binarfinalproject.rajawali.dto.flight.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateFlightDto {
    @NotNull
    private UUID sourceAirportId;

    @NotNull
    private UUID destinationAirportId;

    @NotNull
    private UUID airplaneId;

    @NotNull
    private LocalDateTime departureDate;

    @NotNull
    private LocalDateTime arrivalDate;

    @NotNull
    private double economySeatsPrice;

    @NotNull
    private double businessSeatsPrice;

    @NotNull
    private double firstSeatsPrice;

    @NotNull
    private double discount;
}