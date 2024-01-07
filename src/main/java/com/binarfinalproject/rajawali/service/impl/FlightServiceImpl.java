package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.flight.request.CreateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.request.UpdateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResFlightDto;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FlightRepository flightRepository;

    @Override
    public ResFlightDto createFlight(CreateFlightDto request) {
        Flight flight = modelMapper.map(request, Flight.class);
        ResFlightDto resFlightDto = modelMapper.map(flightRepository.save(flight), ResFlightDto.class);

        return resFlightDto;
    }

    @Override
    public ResFlightDto updateFlight(UUID flightId, UpdateFlightDto request) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        Flight existedFlight = flightOnDb.get();
        if (request.getSourceAirport() != null) {
            request.getSourceAirport().ifPresent(existedFlight::setSourceAirport);
        }
        if (request.getDestinationAirport() != null) {
            request.getDestinationAirport().ifPresent(existedFlight::setDestinationAirport);
        }
        if (request.getAirplane() != null) {
            request.getAirplane().ifPresent(existedFlight::setAirplane);
        }
        request.getDepartureDate().ifPresent(existedFlight::setDepartureDate);
        request.getArrivalDate().ifPresent(existedFlight::setArrivalDate);
        request.getEconomySeatsPrice().ifPresent(existedFlight::setEconomySeatsPrice);
        request.getBusinessSeatsPrice().ifPresent(existedFlight::setBusinessSeatsPrice);
        request.getFirstSeatsPrice().ifPresent(existedFlight::setFirstSeatsPrice);
        request.getDiscount().ifPresent(existedFlight::setDiscount);

        ResFlightDto resFlightDto = modelMapper.map(flightRepository.save(existedFlight),
                ResFlightDto.class);
        return resFlightDto;
    }

    @Override
    public ResFlightDto getFlightById(UUID flightId) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        ResFlightDto resFlightDto = modelMapper.map(flightOnDb.get(), ResFlightDto.class);
        return resFlightDto;
    }

    @Override
    public ResFlightDto deleteFlight(UUID flightId) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        Flight deletedFlight = flightOnDb.get();
        flightRepository.delete(deletedFlight);
        ResFlightDto resFlightDto = modelMapper.map(deletedFlight, ResFlightDto.class);

        return resFlightDto;
    }

    @Override
    public Page<ResFlightDto> getAllFlights(Specification<Flight> filterQueries, Pageable paginationQueries) {
        Page<Flight> flights = flightRepository.findAll(filterQueries, paginationQueries);
        Page<ResFlightDto> flightsDto = flights
                .map(flightEntity -> modelMapper.map(flightEntity, ResFlightDto.class));
        return flightsDto;
    }
}