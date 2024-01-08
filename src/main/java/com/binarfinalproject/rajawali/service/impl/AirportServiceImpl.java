package com.binarfinalproject.rajawali.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.airport.request.CreateAirportDto;
import com.binarfinalproject.rajawali.dto.airport.request.UpdateAirportDto;
import com.binarfinalproject.rajawali.dto.airport.response.ResAirportDto;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirportRepository;
import com.binarfinalproject.rajawali.service.AirportService;

@Service
public class AirportServiceImpl implements AirportService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AirportRepository airportRepository;

    @Override
    public ResAirportDto createAirport(CreateAirportDto request) {
        Airport airport = modelMapper.map(request, Airport.class);
        ResAirportDto resAirportDto = modelMapper.map(airportRepository.save(airport), ResAirportDto.class);

        return resAirportDto;
    }

    @Override
    public ResAirportDto updateAirport(UUID airportId, UpdateAirportDto request) throws ApiException {
        Optional<Airport> airportOnDb = airportRepository.findById(airportId);

        if (airportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airport with id " + airportId + " is not found.");

        Airport existedAirport = airportOnDb.get();
        if (request.getName().isPresent())
            existedAirport.setName(request.getName().get());
        if (request.getCountry().isPresent())
            existedAirport.setCountry(request.getCountry().get());
        if (request.getCity().isPresent())
            existedAirport.setCity(request.getCity().get());
        if (request.getCityCode().isPresent())
            existedAirport.setCityCode(request.getCityCode().get());

        ResAirportDto resAirportDto = modelMapper.map(airportRepository.save(existedAirport),
                ResAirportDto.class);
        return resAirportDto;
    }

    @Override
    public ResAirportDto getAirportById(UUID airportId) throws ApiException {
        Optional<Airport> airportOnDb = airportRepository.findById(airportId);

        if (airportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airport with id " + airportId + " is not found.");

        ResAirportDto resAirportDto = modelMapper.map(airportOnDb.get(), ResAirportDto.class);
        return resAirportDto;
    }

    @Override
    public ResAirportDto deleteAirport(UUID airportId) throws ApiException {
        Optional<Airport> airportOnDb = airportRepository.findById(airportId);

        if (airportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airport with id " + airportId + " is not found.");

        Airport deletedAirport = airportOnDb.get();
        deletedAirport.setDeletedAt(LocalDateTime.now());
        ResAirportDto resAirportDto = modelMapper.map(airportRepository.save(deletedAirport), ResAirportDto.class);

        return resAirportDto;
    }

    @Override
    public Page<ResAirportDto> getAllAirports(Specification<Airport> filterQueries, Pageable paginationQueries) {
        Page<Airport> airports = airportRepository.findAll(filterQueries, paginationQueries);
        Page<ResAirportDto> productsDto = airports
                .map(productEntity -> modelMapper.map(productEntity, ResAirportDto.class));
        return productsDto;
    }

}
