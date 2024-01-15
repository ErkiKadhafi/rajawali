package com.binarfinalproject.rajawali.dto.reservation.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightDetailsDto {
    @NotBlank
    private String flightId;

    @NotNull
    private Boolean useAssurance;

    @NotNull
    @NotEmpty(message = "array cannot be empty")
    private List<@Valid PassengerDto> passengers;
}
