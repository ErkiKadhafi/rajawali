package com.binarfinalproject.rajawali.dto.flight.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
class ResDepartureAirportDto {
    private String id;

    private String name;

    private String country;

    private String city;

    private String cityCode;
}

@Data
class ResDepartureAirplaneDto {
    private String id;

    private String airplaneCode;
}

@Data
public class ResDepartureDto {
    private UUID id;

    private ResDepartureAirportDto sourceAirport;

    private ResDepartureAirportDto destinationAirport;

    private ResDepartureAirplaneDto airplane;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private Double seatPrice;

    private Double totalPrice;

    private Double discount;

    private int economyAvailableSeats;

    private int businessAvailableSeats;

    private int firstAvailableSeats;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}