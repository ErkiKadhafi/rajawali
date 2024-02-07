package com.binarfinalproject.rajawali.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.auth.request.UpdateProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResUserDto;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.ReservationRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;
    
    @Override
    public Page<ResUserDto> getAllUsers(Specification<User> filterQueries, Pageable paginationQueries) {
        Page<User> users = userRepository.findAll(filterQueries, paginationQueries);
        Page<ResUserDto> resUserDtos = users
                .map(userEntity -> {
                    ResUserDto resUserDto = modelMapper.map(userEntity, ResUserDto.class);
                    resUserDto.setReservationsMade(reservationRepository.countByUserId(userEntity.getId()));
                    return resUserDto;
                });
        return resUserDtos;
    }

    @Override
    public ResProfileDto findProfileByUserId(UUID userId) throws ApiException {
        Optional<User> userOnDb = userRepository.findById(userId);
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User id  '" + userId + "' is not found.");

        ResProfileDto resProfileDto = modelMapper.map(userOnDb.get(), ResProfileDto.class);
        return resProfileDto;
    }

    @Override
    public ResProfileDto updateProfile(UpdateProfileDto request) throws ApiException {
        Optional<User> userOnDb = userRepository.findByEmail(request.getEmail());
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Email  '" + request.getEmail() + "' is not found.");

        User user = userOnDb.get();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        ResProfileDto resProfileDto = modelMapper.map(userRepository.save(user), ResProfileDto.class);
        return resProfileDto;
    }

}
