package com.binarfinalproject.rajawali.dto.flight.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.validator.constraints.UUID;

@Data
public class UpdateFlightDto {
    private Optional<@UUID String> sourceAirportId;

    private Optional<@UUID String> destinationAirportId;

    private Optional<@UUID String> airplaneId;
    
    private Optional<String> sourceTerminal;

    private Optional<String> destinationTerminal;

    private Optional<String> thumbnailUrl;

    private Optional<LocalDateTime> departureDate;

    private Optional<LocalDateTime> arrivalDate;

    private Optional<Double> economySeatsPrice;

    private Optional<Double> businessSeatsPrice;

    private Optional<Double> firstSeatsPrice;

    private Optional<Double> discount;

    private Optional<Integer> points;
}