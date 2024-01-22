package com.binarfinalproject.rajawali.dto.tourist_destination.response;

import java.util.UUID;

import lombok.Data;

@Data
public class ResTouristDestinationDto {
    private UUID id;

    private String thumbnailUrl;

    private String sourceCity;

    private String sourceCityCode;

    private String destinationCity;

    private String destinationCityCode;

    private double startFromPrice;
}
