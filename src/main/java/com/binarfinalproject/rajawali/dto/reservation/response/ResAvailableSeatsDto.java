package com.binarfinalproject.rajawali.dto.reservation.response;

import java.util.List;

import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;

import lombok.Data;

@Data
public class ResAvailableSeatsDto {
    private String classType;

    private Integer seatPerCol;

    private List<ResSeatDto> seats;
}
