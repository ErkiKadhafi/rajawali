package com.binarfinalproject.rajawali.dto.flight.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
class ResDetailDepartureAirportDto {
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
public class ResDetailDepartureDto {
    private UUID id;

    private ResDetailDepartureAirportDto sourceAirport;

    private ResDetailDepartureAirportDto destinationAirport;

    private ResDepartureAirplaneDto airplane;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private String classType;

    private Double normalSeatPrice;

    private Double normalTotalPrice;

    private Double safeSeatPrice;

    private Double safeTotalPrice;

    private Double discount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int points;
}