package com.binarfinalproject.rajawali.dto.airplane.request;

import lombok.Data;

import java.util.Optional;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class UpdateAirplaneDto {
    private Optional<@Size(min = 4, message = "must has 4 characters") @Size(max = 4, message = "must has 4 characters") String> airplaneCode;

    private Optional<@Positive Integer> economySeats;

    private Optional<@Positive Integer> businessSeats;

    private Optional<@Positive Integer> firstSeats;

    private Optional<@Positive Integer> economySeatsPerCol;

    private Optional<@Positive Integer> businessSeatsPerCol;

    private Optional<@Positive Integer> firstSeatsPerCol;
}
