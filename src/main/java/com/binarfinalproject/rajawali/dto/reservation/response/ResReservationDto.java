package com.binarfinalproject.rajawali.dto.reservation.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
class ContactDetailsDto {
    private String id;

    private String genderType;

    private String fullname;

    private String email;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

@Data
public class ResReservationDto {
    private String flightId;

    private String classType;

    private Double seatPrice;

    private Double totalPrice;

    private ContactDetailsDto contactDetails;

    private List<ResPassengerDto> passengers;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
