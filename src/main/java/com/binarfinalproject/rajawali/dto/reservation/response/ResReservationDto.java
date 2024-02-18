package com.binarfinalproject.rajawali.dto.reservation.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
class ResReservationPromo {
    private String code;

    private Double discountPercentage;
}

@Data
class ResReservationUser {
    private String id;

    private String fullName;

    private String email;
}

@Data
class ResReservationPayment {
    private String id;
}

@Data
public class ResReservationDto {
    private String id;

    private ResReservationUser user;

    private ResReservationPromo promo;

    private ResReservationPayment payment;

    private String classType;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private Double totalPrice;

    private String paymentStatus;

    private List<ResPassengerDto> passengers;

    private List<ResFlightDetailsDto> flightDetailList;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;
}