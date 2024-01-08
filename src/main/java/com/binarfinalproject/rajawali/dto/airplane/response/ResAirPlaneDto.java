package com.binarfinalproject.rajawali.dto.airplane.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResAirplaneDto {
    private String id;

    private String airplaneCode;

    private Integer economySeats;

    private Integer businessSeats;

    private Integer firstSeats;

    private Integer economySeatsPerCol;

    private Integer businessSeatsPerCol;

    private Integer firstSeatsPerCol;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
