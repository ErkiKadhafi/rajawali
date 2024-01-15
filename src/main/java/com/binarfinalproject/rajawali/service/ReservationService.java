package com.binarfinalproject.rajawali.service;

import java.util.List;
import java.util.UUID;

import com.binarfinalproject.rajawali.dto.reservation.request.CreateReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface ReservationService {
    List<ResSeatDto> getAvailableSeats(UUID flightId, Seat.ClassType classType) throws ApiException;

    ResReservationDto createReservation(CreateReservationDto request) throws ApiException;
}
