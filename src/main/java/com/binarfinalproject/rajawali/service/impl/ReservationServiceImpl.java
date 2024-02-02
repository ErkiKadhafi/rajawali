package com.binarfinalproject.rajawali.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.binarfinalproject.rajawali.dto.reservation.request.PassengerDetailsDto;
import com.binarfinalproject.rajawali.dto.reservation.request.PassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResAvailableSeatsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResFlightDetailsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResListReservationDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResPassengerDetailsDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResPassengerDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResPassengerMealDto;
import com.binarfinalproject.rajawali.dto.reservation.response.ResReservationDto;
import com.binarfinalproject.rajawali.dto.seat.response.ResSeatDto;
import com.binarfinalproject.rajawali.entity.Flight;
import com.binarfinalproject.rajawali.entity.Meal;
import com.binarfinalproject.rajawali.entity.Notification;
import com.binarfinalproject.rajawali.entity.Passenger;
import com.binarfinalproject.rajawali.entity.PassengerMeal;
import com.binarfinalproject.rajawali.entity.Payment;
import com.binarfinalproject.rajawali.entity.Promo;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.entity.ReservationDetails;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.entity.Notification.NotificationType;
import com.binarfinalproject.rajawali.entity.Passenger.AgeType;
import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.FlightRepository;
import com.binarfinalproject.rajawali.repository.MealRepository;
import com.binarfinalproject.rajawali.repository.NotificationRepository;
import com.binarfinalproject.rajawali.repository.PassengerMealRepository;
import com.binarfinalproject.rajawali.repository.PassengerRepository;
import com.binarfinalproject.rajawali.repository.PaymentRepository;
import com.binarfinalproject.rajawali.repository.PromoRepository;
import com.binarfinalproject.rajawali.repository.ReservationDetailsRepository;
import com.binarfinalproject.rajawali.repository.ReservationRepository;
import com.binarfinalproject.rajawali.repository.SeatRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
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

    @Autowired
    MealRepository mealRepository;

    @Autowired
    PassengerMealRepository passengerMealRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PromoRepository promoRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

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
        Reservation reservation = modelMapper.map(request, Reservation.class);
        if (request.getUserId() != null && request.getUserId() != "") {
            Optional<User> userOnDb = userRepository.findById(UUID.fromString(request.getUserId()));
            if (userOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "User with id " + request.getUserId() + " is not found.");

            reservation.setUser(userOnDb.get());

            String dateFormatted = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
            Notification notification = new Notification();
            notification.setUser(userOnDb.get());
            notification.setNotificationType(NotificationType.CREATE_RESERVATION);
            notification.setDescription(
                    "You just created a reservation on " + dateFormatted
                            + ", check your reservation history to choose your payment method!");
            notificationRepository.save(notification);

            User user = userOnDb.get();
            user.setNotificationIsSeen(false);
            userRepository.save(user);

        }
        Reservation savedReservation = reservationRepository.saveAndFlush(reservation);
        double totalPriceAllFlights = 0;

        List<ResFlightDetailsDto> listResFlightDtos = new ArrayList<>();
        for (FlightDetailsDto fd : request.getFlightDetailList()) {
            // check if the flight id is exist
            Optional<Flight> flightOnDb = flightRepository.findById(UUID.fromString(fd.getFlightId()));
            if (flightOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Flight with id " + fd.getFlightId() + " is not found.");

            Flight flight = flightOnDb.get();
            ReservationDetails reservationDetails = modelMapper.map(fd, ReservationDetails.class);
            reservationDetails.setReservation(savedReservation);
            reservationDetails.setFlight(flight);
            ReservationDetails savedReservationDetails = reservationDetailsRepository.saveAndFlush(reservationDetails);

            // update available seats
            double totalPrice = 0, seatPrice = 0;
            if (request.getClassType().equals(ClassType.ECONOMY.name())) {
                seatPrice = flight.getEconomySeatsPrice();
                flight.setEconomyAvailableSeats(flight.getEconomyAvailableSeats() - request.getPassengerList().size());
            } else if (request.getClassType().equals(ClassType.BUSINESS.name())) {
                seatPrice = flight.getBusinessSeatsPrice();
                flight.setBusinessAvailableSeats(
                        flight.getBusinessAvailableSeats() - request.getPassengerList().size());
            } else {
                seatPrice = flight.getFirstSeatsPrice();
                flight.setFirstAvailableSeats(flight.getFirstAvailableSeats() - request.getPassengerList().size());
            }

            // count for total price of this flight
            for (PassengerDto pd : request.getPassengerList()) {
                if (pd.getAgeType().equals(AgeType.ADULT.name()))
                    totalPrice += seatPrice;
                else if (pd.getAgeType().equals(AgeType.CHILD.name()))
                    totalPrice += seatPrice * 0.9;
                else
                    totalPrice += seatPrice * 0.8;

                if (fd.getUseTravelAssurance())
                    totalPrice += 100000;
                if (fd.getUseBagageAssurance())
                    totalPrice += 13500;
                if (fd.getUseFlightDelayAssurance())
                    totalPrice += 60000;
            }

            // store each passenger
            int index = 0;
            List<ResPassengerDetailsDto> passengerDetailListDto = new ArrayList<>();
            for (PassengerDetailsDto pdd : fd.getPassengerDetailList()) {
                Optional<Seat> seatOnDb = seatRepository.findById(UUID.fromString(pdd.getSeatId()));
                if (seatOnDb.isEmpty())
                    throw new ApiException(HttpStatus.NOT_FOUND,
                            "Seat with id '" + pdd.getSeatId() + "' is not found.");
                if (!seatOnDb.get().getClassType().name().equals(request.getClassType()))
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Seat with id '" + pdd.getSeatId() + "' is not in class " + request.getClassType());

                Passenger passenger = modelMapper.map(request.getPassengerList().get(index), Passenger.class);
                passenger.setReservationDetails(reservationDetails);
                passenger.setSeat(seatOnDb.get());
                passenger.setBagageAddOns(pdd.getBagageAddOns());
                Passenger savedPassenger = passengerRepository.saveAndFlush(passenger);

                totalPrice += pdd.getBagageAddOns() * 50000;

                // count total price for all meals
                List<ResPassengerMealDto> passengerMeals = new ArrayList<>();
                for (String mealId : pdd.getMealsAddOns()) {
                    Optional<Meal> mealOnDb = mealRepository.findById(UUID.fromString(mealId));
                    if (mealOnDb.isEmpty())
                        throw new ApiException(HttpStatus.NOT_FOUND,
                                "Meal with id '" + mealId + "' is not found.");

                    totalPrice += mealOnDb.get().getPrice();

                    PassengerMeal passengerMeal = new PassengerMeal();
                    passengerMeal.setMeal(mealOnDb.get());
                    passengerMeal.setPassenger(savedPassenger);
                    passengerMealRepository.save(passengerMeal);
                    passengerMeals.add(modelMapper.map(mealOnDb.get(), ResPassengerMealDto.class));
                }

                ResPassengerDetailsDto resPassengerDetailsDto = new ResPassengerDetailsDto();
                resPassengerDetailsDto.setBagageAddOns(pdd.getBagageAddOns());
                resPassengerDetailsDto.setSeatId(seatOnDb.get().getId().toString());
                resPassengerDetailsDto.setMealsAddOns(passengerMeals);
                passengerDetailListDto.add(modelMapper.map(resPassengerDetailsDto, ResPassengerDetailsDto.class));

                index++;
            }

            totalPriceAllFlights += totalPrice;
            flightRepository.saveAndFlush(flight);

            savedReservationDetails.setSeatPrice(seatPrice);
            savedReservationDetails.setTotalPrice(totalPrice);
            ReservationDetails updatedReservationDetails = reservationDetailsRepository
                    .saveAndFlush(savedReservationDetails);

            ResFlightDetailsDto resFlightDetailsDto = modelMapper.map(updatedReservationDetails,
                    ResFlightDetailsDto.class);
            resFlightDetailsDto.setFlightId(flight.getId().toString());
            resFlightDetailsDto.setSeatPrice(seatPrice);
            resFlightDetailsDto.setTotalPrice(totalPrice);
            resFlightDetailsDto.setPassengerDetailList(passengerDetailListDto);
            listResFlightDtos.add(resFlightDetailsDto);
        }

        if (request.getPromoCode() != null && request.getPromoCode() != "") {
            Optional<Promo> promoOnDb = promoRepository.findByCode(request.getPromoCode());
            if (promoOnDb.isEmpty())
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "Promo code '" + request.getPromoCode() + "' is not found.");
            totalPriceAllFlights -= totalPriceAllFlights * promoOnDb.get().getDiscountPercentage();
            savedReservation.setPromo(promoOnDb.get());
        }
        savedReservation.setTotalPrice(totalPriceAllFlights);
        savedReservation.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        reservationRepository.save(savedReservation);
        List<ResPassengerDto> resPassengerListDto = new ArrayList<>();
        request.getPassengerList().forEach(p -> resPassengerListDto.add(modelMapper.map(p, ResPassengerDto.class)));

        ResReservationDto resReservationDto = modelMapper.map(reservationRepository.save(savedReservation),
                ResReservationDto.class);

        resReservationDto.setPaymentStatus("Waiting for Payment");
        resReservationDto.setFlightDetailList(listResFlightDtos);
        resReservationDto.setPassengers(resPassengerListDto);
        resReservationDto.setTotalPrice(totalPriceAllFlights);

        return resReservationDto;
    }

    @Override
    public Page<ResListReservationDto> getAllReservations(Specification<Reservation> filterQueries,
            Pageable paginationQueries) throws ApiException {
        Page<Reservation> reservations = reservationRepository.findAll(filterQueries, paginationQueries);

        Page<ResListReservationDto> reservationsDto = reservations
                .map(reservationEntity -> {
                    ResListReservationDto resListReservationDto = modelMapper.map(reservationEntity,
                            ResListReservationDto.class);
                    Payment payment = reservationEntity.getPayment();
                    if (payment != null) {
                        if (!payment.getIsPaid()) {
                            if (payment.getReservation().getExpiredAt().isAfter(LocalDateTime.now()))
                                resListReservationDto.setPaymentStatus("Waiting for Payment");
                            else
                                resListReservationDto.setPaymentStatus("Purchase Canceled");
                        } else if (payment.getIsApproved() == null)
                            resListReservationDto.setPaymentStatus("Purchase Pending");
                        else if (payment.getIsApproved() == true)
                            resListReservationDto.setPaymentStatus("Purchase Successful");
                        else if (payment.getIsApproved() == false)
                            resListReservationDto.setPaymentStatus("Purchase Canceled");
                    }

                    return resListReservationDto;
                });

        return reservationsDto;
    }

    @Override
    public ResReservationDto getReservationById(UUID reservationId) throws ApiException {
        Optional<Reservation> reservationOnDb = reservationRepository.findById(reservationId);
        if (reservationOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Reservation with id '" + reservationId + "' is not found.");

        ResReservationDto resReservationDto = modelMapper.map(reservationOnDb.get(),
                ResReservationDto.class);

        Payment payment = reservationOnDb.get().getPayment();
        if (payment != null) {
            if (!payment.getIsPaid()) {
                if (payment.getReservation().getExpiredAt().isAfter(LocalDateTime.now()))
                    resReservationDto.setPaymentStatus("Waiting for Payment");
                else
                    resReservationDto.setPaymentStatus("Purchase Canceled");
            } else if (payment.getIsApproved() == null)
                resReservationDto.setPaymentStatus("Purchase Pending");
            else if (payment.getIsApproved() == true)
                resReservationDto.setPaymentStatus("Purchase Successful");
            else if (payment.getIsApproved() == false)
                resReservationDto.setPaymentStatus("Purchase Canceled");
        }

        List<ReservationDetails> reservationDetails = reservationDetailsRepository
                .findByReservationId(reservationOnDb.get().getId());

        List<Passenger> passengers = passengerRepository
                .findPassengersGroupedByReservationDetailsId(reservationDetails.get(0).getId());
        List<ResPassengerDto> passengerDtos = passengers
                .stream()
                .map(p -> {
                    ResPassengerDto resPassengerDto = modelMapper
                            .map(p, ResPassengerDto.class);
                    return resPassengerDto;
                })
                .collect(Collectors.toList());

        resReservationDto.setPassengers(passengerDtos);

        resReservationDto.setFlightDetailList(reservationDetails.stream().map(rd -> {
            ResFlightDetailsDto resFlightDetailsDto = modelMapper.map(rd, ResFlightDetailsDto.class);
            resFlightDetailsDto.setFlightId(rd.getFlight().getId().toString());

            List<ResPassengerDetailsDto> resPassengerDetailsDtoList = rd
                    .getPassengers()
                    .stream()
                    .map(p -> {
                        ResPassengerDetailsDto resPassengerDetailsDto = modelMapper.map(p,
                                ResPassengerDetailsDto.class);
                        resPassengerDetailsDto.setSeatId(p.getSeat().getId().toString());

                        List<ResPassengerMealDto> resPassengerMealDtos = p.getPassengerMeals()
                                .stream()
                                .map(pm -> {
                                    ResPassengerMealDto resPassengerMealDto = modelMapper.map(pm,
                                            ResPassengerMealDto.class);
                                    resPassengerMealDto.setName(pm.getMeal().getName());
                                    resPassengerMealDto.setPrice(pm.getMeal().getPrice());
                                    return resPassengerMealDto;
                                })
                                .collect(Collectors.toList());

                        resPassengerDetailsDto.setMealsAddOns(resPassengerMealDtos);

                        return resPassengerDetailsDto;
                    })
                    .collect(Collectors.toList());
            resFlightDetailsDto.setPassengerDetailList(resPassengerDetailsDtoList);

            return resFlightDetailsDto;
        }).collect(Collectors.toList()));

        return resReservationDto;
    }

}
