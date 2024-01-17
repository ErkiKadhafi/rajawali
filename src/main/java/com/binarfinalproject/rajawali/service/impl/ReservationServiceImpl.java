package com.binarfinalproject.rajawali.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binarfinalproject.rajawali.dto.reservation.request.CreateReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.request.FlightDetailsDto;
import com.binarfinalproject.rajawali.dto.reservation.request.PassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResAvailableSeatsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResFlightDetailsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResListReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResPassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Passenger;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.entity.ReservationDetails;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.entity.Passenger.AgeType;
import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.repository.PassengerRepository;
import com.binarfinalproject.rajawali.repository.ReservationDetailsRepository;
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
    ReservationDetailsRepository reservationDetailsRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Override
    public ResAvailableSeatsDto getAvailableSeats(UUID flightId, Seat.ClassType classType) throws ApiException {
        Optional<Flight> flightOnDb = flightRepository.findById(flightId);

        if (flightOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Flight with id " + flightId + " is not found.");

        int seatPerCol;
        if (classType.name().equals(ClassType.ECONOMY.name()))
            seatPerCol = flightOnDb.get().getAirplane().getEconomySeatsPerCol();
        else if (classType.name().equals(ClassType.BUSINESS.name()))
            seatPerCol = flightOnDb.get().getAirplane().getBusinessSeatsPerCol();
        else
            seatPerCol = flightOnDb.get().getAirplane().getFirstSeatsPerCol();

        List<Seat> allSeats = seatRepository.findByAirplaneIdAndClassType(flightOnDb.get().getAirplane().getId(),
                classType);
        List<Seat> reservedSeats = new ArrayList<>();

        List<ReservationDetails> reservationDetails = reservationDetailsRepository
                .findByFlightId(flightOnDb.get().getId());
        for (ReservationDetails rd : reservationDetails) {
            List<Passenger> passengers = passengerRepository.findByReservationDetailsId(rd.getId());
            for (Passenger p : passengers) {
                reservedSeats.add(p.getSeat());
            }
        }

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
        ResAvailableSeatsDto resAvailableSeatsDto = new ResAvailableSeatsDto();
        resAvailableSeatsDto.setClassType(classType.name());
        resAvailableSeatsDto.setSeatPerCol(seatPerCol);
        resAvailableSeatsDto.setSeats(resSeatDtos);
        return resAvailableSeatsDto;
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResReservationDto createReservation(CreateReservationDto request) throws ApiException {
        // store the reservation based on contact details
        Reservation reservation = modelMapper.map(request, Reservation.class);
        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);
        double totalPriceAllFlights = 0;

        List<ResFlightDetailsDto> listResFlightDtos = new ArrayList<>();
        for (FlightDetailsDto fd : request.getFlightDetails()) {
            // check if the flight id is exist
            Optional<Flight> flightOnDb = flightRepository.findById(UUID.fromString(fd.getFlightId()));
            if (flightOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Flight with id " + fd.getFlightId() + " is not found.");

            Flight flight = flightOnDb.get();
            ReservationDetails reservationDetails = new ReservationDetails();
            reservationDetails.setReservation(savedReservation);
            reservationDetails.setFlight(flight);
            reservationDetails.setUseAssurance(fd.getUseAssurance());

            // update available seats
            double totalPrice = 0, seatPrice = 0;
            if (request.getClassType().equals(ClassType.ECONOMY.name())) {
                seatPrice = flight.getEconomySeatsPrice();
                flight.setEconomyAvailableSeats(flight.getEconomyAvailableSeats() - fd.getPassengers().size());
            } else if (request.getClassType().equals(ClassType.BUSINESS.name())) {
                seatPrice = flight.getBusinessSeatsPrice();
                flight.setBusinessAvailableSeats(flight.getBusinessAvailableSeats() - fd.getPassengers().size());
            } else {
                seatPrice = flight.getFirstSeatsPrice();
                flight.setFirstAvailableSeats(flight.getFirstAvailableSeats() - fd.getPassengers().size());
            }
            for (PassengerDto pd : fd.getPassengers()) {
                if (pd.getAgeType().equals(AgeType.ADULT.name()))
                    totalPrice += seatPrice;
                else if (pd.getAgeType().equals(AgeType.CHILD.name()))
                    totalPrice += seatPrice * 0.9;
                else
                    totalPrice += seatPrice * 0.8;
            }
            if (fd.getUseAssurance())
                totalPrice += 300000;
            totalPriceAllFlights += totalPrice;
            flightRepository.saveAndFlush(flight);
            reservationDetails.setSeatPrice(seatPrice);
            reservationDetails.setTotalPrice(totalPrice);
            ReservationDetails savedReservationDetails = reservationDetailsRepository.saveAndFlush(reservationDetails);

            // store each passenger
            List<ResPassengerDto> listPassengerDtos = new ArrayList<>();
            for (PassengerDto pd : fd.getPassengers()) {
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
                passenger.setReservationDetails(reservationDetails);
                passenger.setSeat(seatOnDb.get());

                listPassengerDtos.add(modelMapper.map(passengerRepository.saveAndFlush(passenger),
                        ResPassengerDto.class));
            }
            ResFlightDetailsDto resFlightDetailsDto = modelMapper.map(savedReservationDetails,
                    ResFlightDetailsDto.class);
            resFlightDetailsDto.setSeatPrice(seatPrice);
            resFlightDetailsDto.setTotalPrice(totalPrice);
            resFlightDetailsDto.setPassengers(listPassengerDtos);
            listResFlightDtos.add(resFlightDetailsDto);
        }

        // map the response
        savedReservation.setTotalPrice(totalPriceAllFlights);
        reservationRepository.save(savedReservation);
        ResReservationDto resReservationDto = modelMapper.map(reservationRepository.save(savedReservation),
                ResReservationDto.class);
        resReservationDto.setFlightDetails(listResFlightDtos);
        resReservationDto.setTotalPrice(totalPriceAllFlights);

        return resReservationDto;
    }

    @Override
    public Page<ResListReservationDto> getAllReservations(Specification<Reservation> filterQueries,
            Pageable paginationQueries) throws ApiException {
        Page<Reservation> reservations = reservationRepository.findAll(filterQueries, paginationQueries);
        Page<ResListReservationDto> reservationsDto = reservations
                .map(productEntity -> modelMapper.map(productEntity, ResListReservationDto.class));
        return reservationsDto;
    }

}
