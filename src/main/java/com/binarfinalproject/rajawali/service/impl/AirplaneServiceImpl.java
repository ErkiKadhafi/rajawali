package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirplaneDto;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirplaneServiceImpl implements AirplaneService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AirplaneRepository airplaneRepository;

    @Override
    public ResAirplaneDto createAirplane(CreateAirplaneDto request) {
        Airplane airplane = modelMapper.map(request, Airplane.class);

        ResAirplaneDto resAirplaneDto = modelMapper.map(airplaneRepository.save(airplane), ResAirplaneDto.class);
        return resAirplaneDto;
    }

    @Override
    public ResAirplaneDto updateAirplane(UUID airplaneId, UpdateAirplaneDto request)
            throws ApiException {
        Optional<Airplane> airplaneOnDb = airplaneRepository.findById(airplaneId);

        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + airplaneId + " is not found.");

        Airplane existedAirplane = airplaneOnDb.get();
        if (request.getAirplaneCode().isPresent())
            existedAirplane.setAirplaneCode(request.getAirplaneCode().get());
        if (request.getEconomySeats().isPresent())
            existedAirplane.setEconomySeats(request.getEconomySeats().get());
        if (request.getBusinessSeats().isPresent())
            existedAirplane.setBusinessSeats(request.getBusinessSeats().get());
        if (request.getFirstSeats().isPresent())
            existedAirplane.setFirstSeats(request.getFirstSeats().get());
        if (request.getEconomySeatsPerCol().isPresent())
            existedAirplane.setEconomySeatsPerCol(request.getEconomySeatsPerCol().get());
        if (request.getBusinessSeatsPerCol().isPresent())
            existedAirplane.setBusinessSeatsPerCol(request.getBusinessSeatsPerCol().get());
        if (request.getFirstSeatsPerCol().isPresent())
            existedAirplane.setFirstSeatsPerCol(request.getFirstSeatsPerCol().get());

        ResAirplaneDto resAirplaneDto = modelMapper.map(airplaneRepository.save(existedAirplane),
                ResAirplaneDto.class);
        return resAirplaneDto;
    }

    @Override
    public ResAirplaneDto getAirplaneById(UUID airplaneId) throws ApiException {
        Optional<Airplane> airplaneOnDb = airplaneRepository.findById(airplaneId);

        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + airplaneId + " is not found.");

        ResAirplaneDto resAirplaneDto = modelMapper.map(airplaneRepository.save(airplaneOnDb.get()),
                ResAirplaneDto.class);
        return resAirplaneDto;
    }

    @Override
    public ResAirplaneDto deleteAirplane(UUID airplaneId) throws ApiException {
        Optional<Airplane> airplaneOnDb = airplaneRepository.findById(airplaneId);

        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + airplaneId + " is not found.");
        Airplane deletedAirplane = airplaneOnDb.get();
        deletedAirplane.setDeletedAt(LocalDateTime.now());
        ResAirplaneDto resAirplaneDto = modelMapper.map(airplaneRepository.save(deletedAirplane), ResAirplaneDto.class);

        return resAirplaneDto;
    }

    @Override
    public Page<ResAirplaneDto> getAllAirplanes(Specification<Airplane> filterQueries, Pageable paginationQueries) {
        Page<Airplane> airplanes = airplaneRepository.findAll(filterQueries, paginationQueries);

        return airplanes.map(d -> modelMapper.map(d, ResAirplaneDto.class));
    }

}
