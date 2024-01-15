package com.binarfinalproject.rajawali.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.binarfinalproject.rajawali.dto.reservation.request.CreateReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.ReservationService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @GetMapping("/available-seats/{flightId}/{classType}")
    public ResponseEntity<Object> getAvailableSeats(@PathVariable UUID flightId,
            @PathVariable Seat.ClassType classType) {
        try {
            List<ResSeatDto> response = reservationService.getAvailableSeats(flightId, classType);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "The available seats has successfully created!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createReservation(@Valid @RequestBody CreateReservationDto request) {
        try {
            ResReservationDto response = reservationService.createReservation(request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Reservation has successfully created!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
