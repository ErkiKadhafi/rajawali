package com.binarfinalproject.rajawali.dto.request;

import java.util.Optional;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAirportDto {

    private Optional<String> name;

    private Optional<@Size(min = 3, message = "must has 3 characters") String> country;

    private Optional<@Size(min = 3, message = "must has min 3 characters") String> city;

    private Optional<@Size(min = 3, message = "must has 3 characters") @Size(max = 3, message = "must has 3 characters") String> cityCode;
}
