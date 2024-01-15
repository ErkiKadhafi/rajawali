package com.binarfinalproject.rajawali.dto.flight.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UUID;

@Data
public class CreateFlightDto {
    @NotBlank
    @UUID
    private String sourceAirportId;

    @NotBlank
    @UUID
    private String destinationAirportId;

    @NotBlank
    private String sourceTerminal;

    @NotBlank
    private String destinationTerminal;

    @NotBlank
    @UUID
    private String airplaneId;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private LocalDateTime departureDate;

    @NotNull
    private LocalDateTime arrivalDate;

    @NotNull
    private Double economySeatsPrice;

    @NotNull
    private Double businessSeatsPrice;

    @NotNull
    private Double firstSeatsPrice;

    @NotNull
    private Double discount;

    @NotNull
    @Positive
    private Integer points;
}