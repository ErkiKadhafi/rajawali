package com.binarfinalproject.rajawali.dto.airplane.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(30)
    @Max(42)
    private Integer economySeats;

    @NonNull
    @Positive
    @Min(24)
    @Max(36)
    private Integer businessSeats;

    @NonNull
    @Positive
    @Min(6)
    @Max(18)
    private Integer firstSeats;

    @NonNull
    @Positive
    @Min(2)
    @Max(3)
    private Integer economySeatsPerCol;

    @NonNull
    @Positive
    @Min(2)
    @Max(3)
    private Integer businessSeatsPerCol;

    @NonNull
    @Positive
    @Min(2)
    @Max(3)
    private Integer firstSeatsPerCol;
}
