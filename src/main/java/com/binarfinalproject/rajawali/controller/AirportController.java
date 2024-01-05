package com.binarfinalproject.rajawali.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binarfinalproject.rajawali.dto.request.CreateAirportDto;
import com.binarfinalproject.rajawali.dto.request.UpdateAirportDto;
import com.binarfinalproject.rajawali.dto.response.ResAirportDto;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.AirportService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/airports")
public class AirportController {
    @Autowired
    AirportService airportService;

    @PostMapping
    public ResponseEntity<Object> createAirport(@Valid @RequestBody CreateAirportDto request) {
        try {
            ResAirportDto newAirport = airportService.createAirport(request);

            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airport has successfully created!",
                    newAirport);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{airportId}")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID airportId,
            @Valid @RequestBody UpdateAirportDto request) {
        try {
            ResAirportDto updatedAirport = airportService.updateAirport(airportId, request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airport has successfully edited!",
                    updatedAirport);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{airportId}")
    public ResponseEntity<Object> getAirportById(@PathVariable UUID airportId) {
        try {
            ResAirportDto airport = airportService.getAirportById(airportId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airport has successfully fetched!",
                    airport);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Airport> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null && !name.isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" +
                                    name.toLowerCase() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
            Page<ResAirportDto> products = airportService.getAllAirports(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Products has successfully fetched!", products);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{airportId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID airportId) {
        try {
            ResAirportDto deletedAirport = airportService.deleteAirport(airportId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airport has successfully deleted!",
                    deletedAirport);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
