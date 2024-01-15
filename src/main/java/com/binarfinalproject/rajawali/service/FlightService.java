package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.flight.request.CreateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.request.UpdateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResDepartureDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResFlightDto;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface FlightService {
    ResFlightDto createFlight(CreateFlightDto request) throws ApiException;

    ResFlightDto updateFlight(UUID flightId, UpdateFlightDto request) throws ApiException;

    ResFlightDto getFlightById(UUID flightId) throws ApiException;

    ResFlightDto deleteFlight(UUID flightId) throws ApiException;

    Page<ResFlightDto> getAllFlights(Specification<Flight> filterQueries, Pageable paginationQueries);

    Page<ResDepartureDto> getAllDepatures(Specification<Flight> filterQueries,
            Pageable paginationQueries,
            Seat.ClassType classType,
            Integer adultsNumber,
            Integer childsNumber,
            Integer infantsNumber) throws ApiException;
}