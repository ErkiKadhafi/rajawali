package com.binarfinalproject.rajawali.dto.seat.response;

import lombok.Data;

@Data
class AirplaneDto {
    private String id;

    private String airplaneCode;
}

@Data
public class ResSeatDto {
    private String id;

    private String seatNo;

    private AirplaneDto airplane;

    private String classType;

    private Boolean isAvailable;
}
