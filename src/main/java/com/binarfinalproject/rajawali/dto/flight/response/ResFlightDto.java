package com.binarfinalproject.rajawali.dto.flight.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
class AirportDto {
    private String id;

    private String name;

    private String country;

    private String city;

    private String cityCode;
}

@Data
class AirplaneDto {
    private String id;

    private String airplaneCode;

}

@Data
public class ResFlightDto {
    private UUID id;

    private AirportDto sourceAirport;

    private AirportDto destinationAirport;

    private AirplaneDto airplane;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private Double economySeatsPrice;

    private Double businessSeatsPrice;

    private Double firstSeatsPrice;

    private Double discount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}