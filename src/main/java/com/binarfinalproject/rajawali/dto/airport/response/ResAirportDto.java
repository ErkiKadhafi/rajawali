package com.binarfinalproject.rajawali.dto.airport.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResAirportDto {
    private String id;

    private String name;

    private String country;

    private String city;

    private String cityCode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
