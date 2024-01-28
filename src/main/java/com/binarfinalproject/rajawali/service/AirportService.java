package com.binarfinalproject.rajawali.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.binarfinalproject.rajawali.dto.airport.request.CreateAirportDto;
import com.binarfinalproject.rajawali.dto.airport.request.UpdateAirportDto;
import com.binarfinalproject.rajawali.dto.airport.response.ResAirportDto;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface AirportService {
    public ResAirportDto createAirport(CreateAirportDto request) throws ApiException;

    public ResAirportDto updateAirport(UUID airportId, UpdateAirportDto request) throws ApiException;

    public ResAirportDto getAirportById(UUID airportId) throws ApiException;

    public ResAirportDto deleteAirport(UUID airportId) throws ApiException;

    public Page<ResAirportDto> getAllAirports(Specification<Airport> filterQueries, Pageable paginationQueries);
}
