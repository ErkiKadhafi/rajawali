package com.binarfinalproject.rajawali.dto.flight.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
class ResFlightAirportDto {
    private String id;

    private String name;

    private String country;

    private String city;

    private String cityCode;
}

@Data
class ResFlightAirplaneDto {
    private String id;

    private String airplaneCode;
}

@Data
public class ResFlightDto {
    private UUID id;

    private ResFlightAirportDto sourceAirport;

    private ResFlightAirportDto destinationAirport;

    private String sourceTerminal;

    private String destinationTerminal;

    private ResFlightAirplaneDto airplane;

    private String thumbnailUrl;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private Double economySeatsPrice;

    private Double businessSeatsPrice;

    private Double firstSeatsPrice;

    private Double discount;

    private int economyAvailableSeats;

    private int businessAvailableSeats;

    private int firstAvailableSeats;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer points;
}