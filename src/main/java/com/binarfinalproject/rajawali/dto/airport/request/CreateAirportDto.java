package com.binarfinalproject.rajawali.dto.airport.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAirportDto {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, message = "must has 3 characters")
    private String country;

    @NotBlank
    @Size(min = 3, message = "must has min 3 characters")
    private String city;

    @NotBlank
    @Size(min = 3, message = "must has 3 characters")
    @Size(max = 3, message = "must has 3 characters")
    private String cityCode;
}
