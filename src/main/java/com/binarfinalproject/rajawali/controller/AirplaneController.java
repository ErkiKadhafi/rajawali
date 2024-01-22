package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirplaneDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.AirplaneService;
import com.binarfinalproject.rajawali.util.ResponseMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/v1/airplanes")
public class AirplaneController {

    @Autowired
    AirplaneService airplaneService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Object> createAirplane(@Valid @RequestBody CreateAirplaneDto request) {
        try {
            ResAirplaneDto response = airplaneService.createAirplane(request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplane has successfully created!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{airplaneId}")
    public ResponseEntity<Object> updateAirplane(@PathVariable UUID airplaneId,
            @Valid @RequestBody UpdateAirplaneDto request) {
        try {
            ResAirplaneDto response = airplaneService.updateAirplane(airplaneId, request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplane has successfully edited!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{airplaneId}")
    public ResponseEntity<Object> getAirplaneById(@PathVariable UUID airplaneId) {
        try {
            ResAirplaneDto response = airplaneService.getAirplaneById(airplaneId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplane has successfully fetched!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllAirplanes(
            @RequestParam(required = false) String airplaneCode,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Airplane> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (airplaneCode != null && !airplaneCode.isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("airplaneCode")), "%" +
                                    airplaneCode.toLowerCase() + "%"));
                    predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });

            Page<ResAirplaneDto> response = airplaneService.getAllAirplanes(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplanes has successfully fetched!",
                    response);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{airplaneId}")
    public ResponseEntity<Object> deleteAirplane(@PathVariable UUID airplaneId) {
        try {
            ResAirplaneDto response = airplaneService.deleteAirplane(airplaneId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplane has successfully deleted!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
