package com.binarfinalproject.rajawali.dto.response;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResAirportDto {
    @NotBlank
    @UUID
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String cityCode;
}
