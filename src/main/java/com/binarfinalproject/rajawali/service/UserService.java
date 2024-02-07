package com.binarfinalproject.rajawali.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.binarfinalproject.rajawali.dto.auth.request.UpdateProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResUserDto;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface UserService {
    public Page<ResUserDto> getAllUsers(Specification<User> filterQueries, Pageable paginationQueries);

    public ResProfileDto findProfileByUserId(UUID userId) throws ApiException;

    public ResProfileDto updateProfile(UpdateProfileDto request) throws ApiException;
}
