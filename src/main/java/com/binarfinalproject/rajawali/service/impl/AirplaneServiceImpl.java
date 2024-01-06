package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.airplane.request.AirPlaneEditRequest;
import com.binarfinalproject.rajawali.dto.airplane.request.AirplaneRequest;
import com.binarfinalproject.rajawali.dto.airplane.response.AirplaneResponse;
import com.binarfinalproject.rajawali.dto.response.ResAirportDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirplaneRepository;
import com.binarfinalproject.rajawali.service.AirplaneService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneRepository airplaneRepository;
    private final ModelMapper modelMapper;

    //constructor injection
    @Autowired
    private AirplaneServiceImpl(AirplaneRepository airplaneRepository,
                                ModelMapper modelMapper){
        this.airplaneRepository = airplaneRepository;
        this.modelMapper = modelMapper;
    }


    //find by id
    @Override
    public Airplane findById(UUID id) throws ApiException {
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Airplane with id " + id + " not found"));
    }

    //create airplane
    @Override
    public AirplaneResponse createData(AirplaneRequest request) {
        Airplane airplane = modelMapper.map(request, Airplane.class);
        AirplaneResponse response = modelMapper.map(airplaneRepository.save(airplane), AirplaneResponse.class);
        return response;
    }

    //update data
    @Override
    public AirplaneResponse updateData(UUID id, AirPlaneEditRequest request) throws ApiException {
        var existData = findById(id);
        if (request.getBusines_seats().isPresent()){
            existData.setBusines_seats(request.getBusines_seats().get());}
        if (request.getEconomy_seats().isPresent()){
            existData.setEconomy_seats(request.getEconomy_seats().get());}
        if (request.getFirst_seats().isPresent()){
            existData.setFirst_seats(request.getFirst_seats().get());}
        if (request.getFirst_seats_per_col().isPresent()){
            existData.setFirst_seats_per_col(request.getFirst_seats_per_col().get());}
        if (request.getEconomy_seats_per_col().isPresent()){
            existData.setEconomy_seats_per_col(request.getEconomy_seats_per_col().get());}
        if (request.getBusines_seats_per_col().isPresent()){
            existData.setBusines_seats_per_col(request.getBusines_seats_per_col().get());}
        AirplaneResponse response = modelMapper.map(airplaneRepository.save(existData), AirplaneResponse.class);
        return response;
    }

    //delete data
    @Override
    public AirplaneResponse deleteData(UUID id) throws ApiException {
        var existData = findById(id);
        airplaneRepository.delete(existData);
        return modelMapper.map(existData, AirplaneResponse.class);
    }

    //get all
    @Override
    public Page<AirplaneResponse> getAllData(Specification<Airplane> filterQueries, Pageable paginationQueries) {
        Page<Airplane> airplanes = airplaneRepository.findAll(filterQueries, paginationQueries);
        Page<AirplaneResponse> response = airplanes.map(d -> modelMapper.map(d, AirplaneResponse.class));
        return response;
    }

}
