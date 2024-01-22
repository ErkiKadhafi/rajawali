package com.binarfinalproject.rajawali.dto.reservation.request;

import java.util.List;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightDetailsDto {
    @NotBlank
    @UUID
    private String flightId;

    @NotNull
    private Boolean useTravelAssurance;

    @NotNull
    private Boolean useBagageAssurance;

    @NotNull
    private Boolean useFlightDelayAssurance;

    @NotNull
    @NotEmpty(message = "passenger detail list cannot be empty")
    private List<@Valid PassengerDetailsDto> passengerDetailList;
}
