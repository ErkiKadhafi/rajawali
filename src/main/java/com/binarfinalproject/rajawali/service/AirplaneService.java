package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.airplane.request.AirPlaneEditRequest;
import com.binarfinalproject.rajawali.dto.airplane.request.AirplaneRequest;
import com.binarfinalproject.rajawali.dto.airplane.response.AirplaneResponse;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface AirplaneService {

    public Airplane findById(UUID id) throws ApiException;
    public AirplaneResponse createData(AirplaneRequest request);
    public AirplaneResponse updateData(UUID id, AirPlaneEditRequest request) throws ApiException;
    public AirplaneResponse deleteData(UUID id) throws ApiException;
    public Page<AirplaneResponse> getAllData(Specification<Airplane> filterQueries, Pageable paginationQueries);

}
