package com.binarfinalproject.rajawali.dto.airplane.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UpdateAirplaneDto {
    @NotBlank
    @Size(min = 4, message = "must has 4 characters")
    @Size(max = 4, message = "must has 4 characters")
    private String airplaneCode;
}
