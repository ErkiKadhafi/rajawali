package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.dto.airplane.request.UpdateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.request.CreateAirplaneDto;
import com.binarfinalproject.rajawali.dto.airplane.response.ResAirplaneDto;
import com.binarfinalproject.rajawali.entity.Airplane;
import com.binarfinalproject.rajawali.entity.Seat;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.AirplaneRepository;
import com.binarfinalproject.rajawali.repository.SeatRepository;
import com.binarfinalproject.rajawali.service.AirplaneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirplaneServiceImpl implements AirplaneService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AirplaneRepository airplaneRepository;

    @Autowired
    SeatRepository seatRepository;

    @Override
    public ResAirplaneDto createAirplane(CreateAirplaneDto request) throws ApiException {
        Optional<Airplane> airplaneOnDb = airplaneRepository.findByAirplaneCode(request.getAirplaneCode());

        if (airplaneOnDb.isPresent())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Airplane with code " + request.getAirplaneCode() + " is already exist.");

        if (request.getEconomySeats() % request.getEconomySeatsPerCol() != 0)
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "The number of economy seats must be a multiple of the number of economy seats per column.");
        if (request.getBusinessSeats() % request.getBusinessSeatsPerCol() != 0)
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "The number of business seats must be a multiple of the number of business seats per column.");
        if (request.getFirstSeats() % request.getFirstSeatsPerCol() != 0)
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "The number of first seats must be a multiple of the number of first seats per column.");

        Airplane airplane = modelMapper.map(request, Airplane.class);
        Airplane newAirplane = airplaneRepository.save(airplane);
        ResAirplaneDto resAirplaneDto = modelMapper.map(newAirplane, ResAirplaneDto.class);

        seatRepository.saveAll(generateSeats(airplane, Seat.ClassType.FIRST,
                0,
                request.getFirstSeats(),
                request.getFirstSeatsPerCol()));
        seatRepository.saveAll(generateSeats(airplane, Seat.ClassType.BUSINESS,
                request.getFirstSeats() / (request.getFirstSeatsPerCol() * 2),
                request.getBusinessSeats(),
                request.getBusinessSeatsPerCol()));
        seatRepository.saveAll(generateSeats(newAirplane, Seat.ClassType.ECONOMY,
                (request.getFirstSeats() / (request.getFirstSeatsPerCol() * 2))
                        + (request.getBusinessSeats() / (request.getBusinessSeatsPerCol() * 2)),
                request.getEconomySeats(),
                request.getEconomySeatsPerCol()));

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
        existedAirplane.setAirplaneCode(request.getAirplaneCode());

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
        airplaneRepository.save(deletedAirplane);

        airplaneRepository.delete(deletedAirplane);
        ResAirplaneDto resAirplaneDto = modelMapper.map(deletedAirplane, ResAirplaneDto.class);

        return resAirplaneDto;
    }

    @Override
    public Page<ResAirplaneDto> getAllAirplanes(Specification<Airplane> filterQueries, Pageable paginationQueries) {
        Page<Airplane> airplanes = airplaneRepository.findAll(filterQueries, paginationQueries);

        return airplanes.map(d -> modelMapper.map(d, ResAirplaneDto.class));
    }

    private List<Seat> generateSeats(Airplane airplane,
            Seat.ClassType classType,
            int seatNumberStart,
            Integer numberOfSeats,
            Integer numberOfSeatsPerCol) {
        List<Seat> seats = new ArrayList<>();
        final int LETTER_A_ASCII = 65;
        final int COLS = 2;
        int numberOfSeatsPerRow = numberOfSeatsPerCol * COLS;

        for (int i = 1; i <= numberOfSeats; i++) {
            int mod = i % numberOfSeatsPerRow;

            double seatNumber = Math.ceil(Double.valueOf(i) / Double.valueOf(numberOfSeatsPerRow)) + seatNumberStart;
            char seatAlphabet = mod == 0 ? (char) (LETTER_A_ASCII + numberOfSeatsPerRow - 1)
                    : (char) (LETTER_A_ASCII + mod - 1);

            Seat newSeat = new Seat();
            newSeat.setAirplane(airplane);
            newSeat.setClassType(classType);
            newSeat.setSeatNo("" + (int) seatNumber + seatAlphabet);
            seats.add(newSeat);
        }
        return seats;
    }

}
