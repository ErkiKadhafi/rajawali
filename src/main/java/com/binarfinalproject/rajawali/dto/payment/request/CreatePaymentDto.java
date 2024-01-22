package com.binarfinalproject.rajawali.dto.payment.request;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePaymentDto {
    @NotBlank
    @UUID
    private String reservationId;

    @NotBlank
    private String method;
}
