package com.binarfinalproject.rajawali.dto.airplane.response;

import com.binarfinalproject.rajawali.entity.Flight;
import lombok.Data;


import java.util.List;
import java.util.UUID;

@Data
public class ResAirPlaneDto {
    private UUID id;
    private Integer economy_seats;
    private Integer busines_seats;
    private Integer first_seats;
    private Integer economy_seats_per_col;
    private Integer busines_seats_per_col;
    private Integer first_seats_per_col;
    private List<Flight> flightList;
}
