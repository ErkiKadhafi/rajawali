package com.binarfinalproject.rajawali.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binarfinalproject.rajawali.dto.reservation.request.CreateReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.request.PassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResPassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;
import com.binarfinalproject.rajawali.entity.ContactDetails;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Passenger;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.entity.Passenger.AgeType;
import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.ContactDetailsRepository;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.repository.PassengerRepository;
import com.binarfinalproject.rajawali.repository.ReservationRepository;
import com.binarfinalproject.rajawali.repository.SeatRepository;
import com.binarfinalproject.rajawali.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ContactDetailsRepository contactDetailsRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Override
    public List<ResSeatDto> getAvailableSeats(UUID flightId, Seat.ClassType classType) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        List<Seat> allSeats = seatRepository.findByAirplaneId(flightOnDb.get().getAirplane().getId());
        List<Seat> reservedSeats = seatRepository.findByAirplaneIdAndClassType(flightOnDb.get().getAirplane().getId(),
                classType);

        List<ResSeatDto> resSeatDtos = allSeats
                .stream()
                .map(s -> {
                    ResSeatDto resSeatDto = modelMapper.map(s, ResSeatDto.class);
                    Optional<Seat> checkSeatAvailability = reservedSeats.stream().filter(rs -> {
                        return rs.getId().equals(s.getId());
                    }).findAny();

                    if (checkSeatAvailability.isPresent()) {
                        resSeatDto.setIsAvailable(false);
                    } else
                        resSeatDto.setIsAvailable(true);
                    return resSeatDto;
                })
                .collect(Collectors.toList());

        return resSeatDtos;
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResReservationDto createReservation(CreateReservationDto request) throws ApiException {
        // check if the flight id is exist
        Optional<Flight> flightOnDb = flightRepository.findById(UUID.fromString(request.getFlightId()));
        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + request.getFlightId() + " is not found.");

        // store the contact details
        Flight flight = flightOnDb.get();
        ContactDetails contactDetails = modelMapper.map(request.getContactDetails(),
                ContactDetails.class);
        ContactDetails savedContactDetails = contactDetailsRepository.saveAndFlush(contactDetails);

        // store the reservation based on contact details
        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setContactDetails(savedContactDetails);
        reservation.setClassType(ClassType.valueOf(request.getClassType()));
        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);

        // update available seats
        double totalPrice = 0, seatPrice = 0;
        if (request.getClassType().equals(ClassType.ECONOMY.name())) {
            seatPrice = flight.getEconomySeatsPrice();
            flight.setEconomyAvailableSeats(flight.getEconomyAvailableSeats() - request.getPassengers().size());
        } else if (request.getClassType().equals(ClassType.BUSINESS.name())) {
            seatPrice = flight.getBusinessSeatsPrice();
            flight.setBusinessAvailableSeats(flight.getBusinessAvailableSeats() - request.getPassengers().size());
        } else {
            seatPrice = flight.getFirstSeatsPrice();
            flight.setFirstAvailableSeats(flight.getFirstAvailableSeats() - request.getPassengers().size());
        }
        flightRepository.saveAndFlush(flight);

        // store each passenger
        List<ResPassengerDto> passengersDto = new ArrayList<>();
        for (PassengerDto pd : request.getPassengers()) {
            Optional<Seat> seatOnDb = seatRepository.findById(UUID.fromString(pd.getSeatId()));
            if (seatOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Seat with id '" + pd.getSeatId() + "' is not found.");
            if (!seatOnDb.get().getClassType().name().equals(request.getClassType()))
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Seat with id '" + pd.getSeatId() + "' is not in class " + request.getClassType());
            if (!seatOnDb.get().getClassType().name().equals(request.getClassType()))
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Seat with id '" + pd.getSeatId() + "' is not in class " + request.getClassType());

            Passenger passenger = modelMapper.map(pd, Passenger.class);
            passenger.setReservation(savedReservation);
            passenger.setSeat(seatOnDb.get());
            if (pd.getAgeType().equals(AgeType.ADULT.name()))
                totalPrice += seatPrice;
            else if (pd.getAgeType().equals(AgeType.CHILD.name()))
                totalPrice += seatPrice * 0.9;
            else
                totalPrice += seatPrice * 0.8;

            passengersDto.add(modelMapper.map(passengerRepository.saveAndFlush(passenger), ResPassengerDto.class));
        }

        // map the response
        savedReservation.setSeatPrice(seatPrice);
        savedReservation.setTotalPrice(totalPrice);
        ResReservationDto resReservationDto = modelMapper.map(reservationRepository.save(savedReservation),
                ResReservationDto.class);
        resReservationDto.setPassengers(passengersDto);

        return resReservationDto;
    }

}
