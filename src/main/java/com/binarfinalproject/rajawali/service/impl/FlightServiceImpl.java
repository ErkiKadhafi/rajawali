package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.flight.request.CreateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.request.UpdateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResFlightDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirplaneRepository;
import com.binarfinalproject.rajawali.repository.AirportRepository;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.service.FlightService;

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
public class FlightServiceImpl implements FlightService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    AirplaneRepository airplaneRepository;

    @Autowired
    FlightRepository flightRepository;

    @Override
    public ResFlightDto createFlight(CreateFlightDto request) throws ApiException {
        Optional<Airport> sourceAirportOnDb = airportRepository.findById(UUID.fromString(request.getSourceAirportId()));
        if (sourceAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Source airport with id " + request.getSourceAirportId() + " is not found.");

        Optional<Airport> destAirportOnDb = airportRepository
                .findById(UUID.fromString(request.getDestinationAirportId()));
        if (destAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Destination airport with id " + request.getDestinationAirportId() + " is not found.");

        Optional<Airplane> airplaneOnDb = airplaneRepository.findById(UUID.fromString(request.getAirplaneId()));
        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + request.getAirplaneId() + " is not found.");

        Flight flight = modelMapper.map(request, Flight.class);
        flight.setSourceAirport(sourceAirportOnDb.get());
        flight.setDestinationAirport(destAirportOnDb.get());
        flight.setAirplane(airplaneOnDb.get());
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
        if (request.getSourceAirportId().isPresent()) {
            Optional<Airport> sourceAirportOnDb = airportRepository
                    .findById(UUID.fromString(request.getSourceAirportId().get()));
            if (sourceAirportOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Source airport with id " + request.getSourceAirportId().get() + " is not found.");
            existedFlight.setSourceAirport(sourceAirportOnDb.get());
        }
        if (request.getDestinationAirportId().isPresent()) {
            Optional<Airport> destAirportOnDb = airportRepository
                    .findById(UUID.fromString(request.getDestinationAirportId().get()));
            if (destAirportOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Destination airport with id " + request.getDestinationAirportId().get() + " is not found.");
            existedFlight.setDestinationAirport(destAirportOnDb.get());
        }
        if (request.getDestinationAirportId().isPresent()) {
            Optional<Airplane> airplaneOnDb = airplaneRepository
                    .findById(UUID.fromString(request.getAirplaneId().get()));
            if (airplaneOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Airplane with id " + request.getAirplaneId().get() + " is not found.");
            existedFlight.setAirplane(airplaneOnDb.get());
        }

        if (existedFlight.getSourceAirport().getId().equals(existedFlight.getDestinationAirport().getId()))
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Source and destination airport can't be the same ");

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
        deletedFlight.setDeletedAt(LocalDateTime.now());
        ResFlightDto resFlightDto = modelMapper.map(flightRepository.save(deletedFlight), ResFlightDto.class);

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