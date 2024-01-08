package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirPlaneDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirplaneRepository;
import com.binarfinalproject.rajawali.service.AirplaneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;
    @Autowired
    private ModelMapper modelMapper;



    //find by id
    @Override
    public Airplane findById(UUID id) throws ApiException {
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Airplane with id " + id + " not found"));
    }

    //create airplane
    @Override
    public ResAirPlaneDto createAirplane(CreateAirplaneDto request) {
        Airplane airplane = modelMapper.map(request, Airplane.class);
        return modelMapper.map(airplaneRepository.save(airplane), ResAirPlaneDto.class);

    }

    //update data
    @Override
    public ResAirPlaneDto updateAirplane(UUID id, UpdateAirplaneDto request) throws ApiException {
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
        return modelMapper.map(airplaneRepository.save(existData), ResAirPlaneDto.class);

    }

    //delete data
    @Override
    public ResAirPlaneDto deleteAirplane(UUID id) throws ApiException {
        var existData = findById(id);
        airplaneRepository.delete(existData);
        return modelMapper.map(existData, ResAirPlaneDto.class);
    }

    //get all
    @Override
    public Page<ResAirPlaneDto> getAllAirplane(Specification<Airplane> filterQueries, Pageable paginationQueries) {
        Page<Airplane> airplanes = airplaneRepository.findAll(filterQueries, paginationQueries);
        return airplanes.map(d -> modelMapper.map(d, ResAirPlaneDto.class));

    }

}
