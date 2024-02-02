package com.binarfinalproject.rajawali.dto.flight.request;

import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class UpdateFlightDto {
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
    @DecimalMin("300000.0")
    @DecimalMax("1000000.0")
    private Double economySeatsPrice;

    @NotNull
    @DecimalMin("500000.0")
    @DecimalMax("1500000.0")
    private Double businessSeatsPrice;

    @NotNull
    @DecimalMin("750000.0")
    @DecimalMax("2000000.0")
    private Double firstSeatsPrice;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double discount;

    @NotNull
    @Positive
    @Min(10000)
    @Max(100000)
    private Integer points;
}