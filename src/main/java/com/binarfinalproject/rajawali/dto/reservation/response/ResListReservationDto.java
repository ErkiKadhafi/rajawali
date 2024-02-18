package com.binarfinalproject.rajawali.dto.reservation.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
class ResListReservationPromo {
    private String code;

    private Double discountPercentage;
}

@Data
class ResListReservationUser {
    private String id;

    private String fullName;

    private String email;
}

@Data
class ResListReservationPayment {
    private String id;
}

@Data
public class ResListReservationDto {
    private String id;

    private ResListReservationPromo promo;

    private ResListReservationUser user;

    private ResListReservationPayment payment;

    private String paymentStatus;

    private String classType;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private Double totalPrice;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;
}