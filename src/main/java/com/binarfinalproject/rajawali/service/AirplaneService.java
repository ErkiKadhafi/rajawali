package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirPlaneDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AirplaneService {

    public Airplane findById(UUID id) throws ApiException;
    public ResAirPlaneDto createAirplane(CreateAirplaneDto request);
    public ResAirPlaneDto updateAirplane(UUID id, UpdateAirplaneDto request) throws ApiException;
    public ResAirPlaneDto deleteAirplane(UUID id) throws ApiException;
    public Page<ResAirPlaneDto> getAllAirplane(Specification<Airplane> filterQueries, Pageable paginationQueries);


}
