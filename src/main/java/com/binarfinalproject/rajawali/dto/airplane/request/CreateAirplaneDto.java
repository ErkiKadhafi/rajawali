package com.binarfinalproject.rajawali.dto.airplane.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateAirplaneDto {
    @NotBlank
    @Size(min = 4, message = "must has 4 characters")
    @Size(max = 4, message = "must has 4 characters")
    private String airplaneCode;

    @NonNull
    @Positive
    private Integer economySeats;

    @NonNull
    @Positive
    private Integer businessSeats;

    @NonNull
    @Positive
    private Integer firstSeats;

    @NonNull
    @Positive
    private Integer economySeatsPerCol;

    @NonNull
    @Positive
    private Integer businessSeatsPerCol;

    @NonNull
    @Positive
    private Integer firstSeatsPerCol;
}
