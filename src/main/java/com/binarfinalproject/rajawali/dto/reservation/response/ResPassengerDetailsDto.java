package com.binarfinalproject.rajawali.dto.reservation.response;

import java.util.List;

import lombok.Data;

@Data
public class ResPassengerDetailsDto {
    private String seatId;

    private Integer bagageAddOns;

    private List<ResPassengerMealDto> mealsAddOns;
}
