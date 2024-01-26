package com.binarfinalproject.rajawali.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.binarfinalproject.rajawali.dto.reservation.request.CreateReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResAvailableSeatsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResListReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface ReservationService {
    Page<ResListReservationDto> getAllReservations(Specification<Reservation> filterQueries, Pageable paginationQueries)
            throws ApiException;

    ResAvailableSeatsDto getAvailableSeats(UUID flightId, Seat.ClassType classType) throws ApiException;

    ResReservationDto createReservation(CreateReservationDto request) throws ApiException;
}
