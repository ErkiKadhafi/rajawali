package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirplaneDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AirplaneService {
    public ResAirplaneDto createAirplane(CreateAirplaneDto request) throws ApiException;

    public ResAirplaneDto updateAirplane(UUID airplaneId, UpdateAirplaneDto request) throws ApiException;

    public ResAirplaneDto getAirplaneById(UUID airplaneId) throws ApiException;

    public ResAirplaneDto deleteAirplane(UUID airplaneId) throws ApiException;

    public Page<ResAirplaneDto> getAllAirplanes(Specification<Airplane> filterQueries, Pageable paginationQueries);
}
