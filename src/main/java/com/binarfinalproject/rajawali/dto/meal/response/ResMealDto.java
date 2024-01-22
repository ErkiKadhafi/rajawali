package com.binarfinalproject.rajawali.dto.meal.response;

import java.util.UUID;

import lombok.Data;

@Data
public class ResMealDto {
    private UUID id;

    private String name;

    private String description;

    private Double price;

    private String thumbnailUrl;
}
