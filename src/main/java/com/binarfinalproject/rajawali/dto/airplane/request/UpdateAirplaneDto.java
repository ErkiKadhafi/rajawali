package com.binarfinalproject.rajawali.dto.airplane.request;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdateAirplaneDto {
    private Optional<Integer> economy_seats;
    private Optional<Integer> busines_seats;
    private Optional<Integer> first_seats;
    private Optional<Integer> economy_seats_per_col;
    private Optional<Integer> busines_seats_per_col;
    private Optional<Integer> first_seats_per_col;
}
