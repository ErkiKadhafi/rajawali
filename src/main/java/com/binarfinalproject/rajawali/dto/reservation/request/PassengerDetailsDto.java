package com.binarfinalproject.rajawali.dto.reservation.request;

import java.util.List;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerDetailsDto {
    @NotBlank
    @UUID
    private String seatId;

    @NotNull
    @Max(value = 40, message = "max bagage add-ons is 40")
    private int bagageAddOns;

    private List<String> mealsAddOns;
}
