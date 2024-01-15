package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.flight.request.CreateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.request.UpdateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResDepartureDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResFlightDto;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.FlightService;
import com.binarfinalproject.rajawali.util.ResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/v1/flights")
public class FlightController {
    @Autowired
    FlightService flightService;

    @PostMapping
    public ResponseEntity<Object> createFlight(@Valid @RequestBody CreateFlightDto request) {
        try {
            ResFlightDto newFlight = flightService.createFlight(request);

            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Flight has successfully created!",
                    newFlight);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<Object> updateFlight(@PathVariable UUID flightId,
            @Valid @RequestBody UpdateFlightDto request) {
        try {
            ResFlightDto updatedFlight = flightService.updateFlight(flightId, request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Flight has successfully edited!",
                    updatedFlight);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<Object> getFlightById(@PathVariable UUID flightId) {
        try {
            ResFlightDto flight = flightService.getFlightById(flightId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Flight has successfully fetched!",
                    flight);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllFlights(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Flight> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
            Page<ResFlightDto> flights = flightService.getAllFlights(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Flights has successfully fetched!", flights);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<Object> deleteFlight(@PathVariable UUID flightId) {
        try {
            ResFlightDto deletedFlight = flightService.deleteFlight(flightId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Flight has successfully deleted!",
                    deletedFlight);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/depatures")
    public ResponseEntity<Object> getDepatureFlights(
            @RequestParam(required = true) LocalDate departureDate,
            @RequestParam(required = true) Seat.ClassType classType,
            @RequestParam(required = true) Integer adultsNumber,
            @RequestParam(required = false) Integer childsNumber,
            @RequestParam(required = false) Integer infantsNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;
            final int tempChildsNumber = childsNumber != null ? childsNumber : 0;
            final int tempInfantsNumber = infantsNumber != null ? infantsNumber : 0;

            Map<String, String> classTypeMappedAttr = new HashMap<String, String>();
            classTypeMappedAttr.put(Seat.ClassType.ECONOMY.name(), "economyAvailableSeats");
            classTypeMappedAttr.put(Seat.ClassType.BUSINESS.name(), "businessAvailableSeats");
            classTypeMappedAttr.put(Seat.ClassType.FIRST.name(), "firstAvailableSeats");

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Flight> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("departureDate"), departureDate));
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(classTypeMappedAttr.get(classType.name())),
                                adultsNumber + tempChildsNumber + tempInfantsNumber));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
            Page<ResDepartureDto> flights = flightService.getAllDepatures(filterQueries, paginationQueries, classType,
                    adultsNumber, tempChildsNumber, tempInfantsNumber);

            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Depatures has successfully fetched!", flights);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}