package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.flight.request.CreateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.request.UpdateFlightDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResDepartureDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResDetailDepartureDto;
import com.binarfinalproject.rajawali.dto.flight.response.ResFlightDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.entity.Airport;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.entity.TouristDestination;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirplaneRepository;
import com.binarfinalproject.rajawali.repository.AirportRepository;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.repository.TouristDestinationRepository;
import com.binarfinalproject.rajawali.service.FlightService;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    TouristDestinationRepository touristDestinationRepository;

    @Override
    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    public ResFlightDto createFlight(CreateFlightDto request) throws ApiException {
        // check if source airoport id is exist
        Optional<Airport> sourceAirportOnDb = airportRepository.findById(UUID.fromString(request.getSourceAirportId()));
        if (sourceAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Source airport with id " + request.getSourceAirportId() + " is not found.");

        // check if dest airoport id is exist
        Optional<Airport> destAirportOnDb = airportRepository
                .findById(UUID.fromString(request.getDestinationAirportId()));
        if (destAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Destination airport with id " + request.getDestinationAirportId() + " is not found.");

        // check if airplane id is exist
        Optional<Airplane> airplaneOnDb = airplaneRepository.findById(UUID.fromString(request.getAirplaneId()));
        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + request.getAirplaneId() + " is not found.");

        // create the flight
        Flight flight = modelMapper.map(request, Flight.class);
        flight.setSourceAirport(sourceAirportOnDb.get());
        flight.setDestinationAirport(destAirportOnDb.get());
        flight.setAirplane(airplaneOnDb.get());
        flight.setEconomyAvailableSeats(airplaneOnDb.get().getEconomySeats());
        flight.setBusinessAvailableSeats(airplaneOnDb.get().getBusinessSeats());
        flight.setFirstAvailableSeats(airplaneOnDb.get().getFirstSeats());
        ResFlightDto resFlightDto = modelMapper.map(flightRepository.save(flight), ResFlightDto.class);

        // create new tourist destination
        Optional<TouristDestination> touristDestinationOnDb = touristDestinationRepository
                .findBySourceCityCodeAndDestinationCityCode(
                        sourceAirportOnDb.get().getCityCode(),
                        destAirportOnDb.get().getCityCode());
        TouristDestination touristDestination;
        if (touristDestinationOnDb.isEmpty()) {
            touristDestination = new TouristDestination();
            touristDestination.setThumbnailUrl(request.getThumbnailUrl());
            touristDestination.setSourceCity(sourceAirportOnDb.get().getCity());
            touristDestination.setSourceCityCode(sourceAirportOnDb.get().getCityCode());
            touristDestination.setDestinationCity(destAirportOnDb.get().getCity());
            touristDestination.setDestinationCityCode(destAirportOnDb.get().getCityCode());
            touristDestination.setStartFromPrice(flight.getEconomySeatsPrice());
        } else {
            touristDestination = touristDestinationOnDb.get();
            if (touristDestination.getStartFromPrice() > flight.getEconomySeatsPrice())
                touristDestination.setStartFromPrice(flight.getEconomySeatsPrice());
        }
        touristDestinationRepository.save(touristDestination);

        return resFlightDto;
    }

    @Override
    public ResFlightDto updateFlight(UUID flightId, UpdateFlightDto request) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        Flight existedFlight = flightOnDb.get();
        Optional<Airport> sourceAirportOnDb = airportRepository
                .findById(UUID.fromString(request.getSourceAirportId()));
        if (sourceAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Source airport with id " + request.getSourceAirportId() + " is not found.");
        existedFlight.setSourceAirport(sourceAirportOnDb.get());

        Optional<Airport> destAirportOnDb = airportRepository
                .findById(UUID.fromString(request.getDestinationAirportId()));
        if (destAirportOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Destination airport with id " + request.getDestinationAirportId() + " is not found.");
        existedFlight.setDestinationAirport(destAirportOnDb.get());

        Optional<Airplane> airplaneOnDb = airplaneRepository
                .findById(UUID.fromString(request.getAirplaneId()));
        if (airplaneOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Airplane with id " + request.getAirplaneId() + " is not found.");
        existedFlight.setAirplane(airplaneOnDb.get());

        if (existedFlight.getSourceAirport().getId().equals(existedFlight.getDestinationAirport().getId()))
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Source and destination airport can't be the same ");

        existedFlight.setSourceTerminal(request.getSourceTerminal());
        existedFlight.setDestinationTerminal(request.getDestinationTerminal());
        existedFlight.setDepartureDate(request.getDepartureDate());
        existedFlight.setArrivalDate(request.getArrivalDate());
        existedFlight.setEconomySeatsPrice(request.getEconomySeatsPrice());
        existedFlight.setBusinessSeatsPrice(request.getBusinessSeatsPrice());
        existedFlight.setFirstSeatsPrice(request.getFirstSeatsPrice());
        existedFlight.setDiscount(request.getDiscount());
        existedFlight.setPoints(request.getPoints());

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
        flightRepository.save(deletedFlight);
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

    @Override
    public Page<ResDepartureDto> getAllDepatures(Specification<Flight> filterQueries, Pageable paginationQueries,
            Seat.ClassType classType, Integer adultsNumber, Integer childsNumber, Integer infantsNumber)
            throws ApiException {

        Page<Flight> departures = flightRepository.findAll(filterQueries, paginationQueries);

        Converter<Double, Double> convertTotalPrice = ctx -> ctx.getSource() == null ? null
                : (ctx.getSource() * adultsNumber) + (ctx.getSource() * childsNumber * 0.9)
                        + (ctx.getSource() * infantsNumber * 0.8);
        TypeMap<Flight, ResDepartureDto> typeMap = modelMapper.getTypeMap(Flight.class,
                ResDepartureDto.class);
        if (typeMap == null) {
            typeMap = modelMapper
                    .createTypeMap(Flight.class, ResDepartureDto.class);
        }
        typeMap.addMappings(mapper -> mapper
                .using(convertTotalPrice)
                .map(src -> {
                    if (classType.name().equals("ECONOMY"))
                        return src.getEconomySeatsPrice();
                    else if (classType.name().equals("BUSINESS"))
                        return src.getBusinessSeatsPrice();
                    else
                        return src.getFirstSeatsPrice();
                }, ResDepartureDto::setTotalPrice))
                .addMappings(mapper -> mapper.map(src -> {
                    if (classType.name().equals("ECONOMY"))
                        return src.getEconomySeatsPrice();
                    else if (classType.name().equals("BUSINESS"))
                        return src.getBusinessSeatsPrice();
                    else
                        return src.getFirstSeatsPrice();
                }, ResDepartureDto::setSeatPrice))
                .addMappings(mapper -> mapper.map(src -> {
                    if (classType.name().equals("ECONOMY"))
                        return src.getEconomyAvailableSeats();
                    else if (classType.name().equals("BUSINESS"))
                        return src.getBusinessAvailableSeats();
                    else
                        return src.getFirstAvailableSeats();
                }, ResDepartureDto::setAvailableSeats))
                .addMappings(mapper -> mapper.map(src -> classType.name(), ResDepartureDto::setClassType));

        Page<ResDepartureDto> flightsDto = departures
                .map(flightEntity -> modelMapper.map(flightEntity, ResDepartureDto.class));

        return flightsDto;
    }

    @Override
    public ResDetailDepartureDto getDepatureFlightsById(
            UUID flightId,
            Seat.ClassType classType,
            Integer adultsNumber,
            Integer childsNumber,
            Integer infantsNumber) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");
        Flight flight = flightOnDb.get();
        ResDetailDepartureDto resDepartureDto = modelMapper.map(flight, ResDetailDepartureDto.class);

        resDepartureDto.setClassType(classType.name());

        double normalSeatPrice, safeSeatPrice;
        if (classType.name().equals("ECONOMY"))
            normalSeatPrice = flight.getEconomySeatsPrice();
        else if (classType.name().equals("BUSINESS"))
            normalSeatPrice = flight.getBusinessSeatsPrice();
        else
            normalSeatPrice = flight.getFirstSeatsPrice();
        safeSeatPrice = normalSeatPrice + 100000;
        resDepartureDto.setNormalSeatPrice(normalSeatPrice);
        resDepartureDto.setSafeSeatPrice(safeSeatPrice);

        double normalTotalPrice = (normalSeatPrice * adultsNumber) + (normalSeatPrice * childsNumber * 0.9)
                + (normalSeatPrice * infantsNumber * 0.8);
        double safeTotalPrice = (safeSeatPrice * adultsNumber) + (safeSeatPrice * childsNumber * 0.9)
                + (safeSeatPrice * infantsNumber * 0.8);
        resDepartureDto.setNormalTotalPrice(normalTotalPrice);
        resDepartureDto.setSafeTotalPrice(safeTotalPrice);
        return resDepartureDto;

    }

}