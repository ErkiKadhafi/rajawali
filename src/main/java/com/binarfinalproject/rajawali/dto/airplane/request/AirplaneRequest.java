package com.binarfinalproject.rajawali.dto.airplane.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class AirplaneRequest {
    @NonNull
    private Integer economy_seats;

    @NonNull
    private Integer busines_seats;

    @NonNull
    private Integer first_seats;

    @NonNull
    private Integer economy_seats_per_col;

    @NonNull
    private Integer busines_seats_per_col;

    @NonNull
    private Integer first_seats_per_col;

}
