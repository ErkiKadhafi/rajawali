package com.binarfinalproject.rajawali.dto.flight.request;

import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.entity.Airport;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
public class UpdateFlightDto {
    private Optional<Airport> sourceAirport;
    private Optional<Airport> destinationAirport;
    private Optional<Airplane> airplane;

    private Optional<LocalDateTime> departureDate;

    private Optional<LocalDateTime> arrivalDate;

    private Optional<Double> economySeatsPrice;

    private Optional<Double> businessSeatsPrice;

    private Optional<Double> firstSeatsPrice;

    private Optional<Double> discount;
}