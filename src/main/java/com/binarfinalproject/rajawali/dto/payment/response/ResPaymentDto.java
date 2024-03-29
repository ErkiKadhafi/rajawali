package com.binarfinalproject.rajawali.dto.payment.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
class ResPaymentReservationDto {
    private String id;

    private String classType;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private double totalPrice;

    private LocalDateTime expiredAt;
}

@Data
public class ResPaymentDto {
    private String id;

    private ResPaymentReservationDto reservation;

    private String method;

    private String receiverNumber;

    private String paymentStatus;

    private LocalDateTime paidAt;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
