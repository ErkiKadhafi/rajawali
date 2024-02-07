package com.binarfinalproject.rajawali.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binarfinalproject.rajawali.dto.auth.request.UpdateProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResProfileDto;
import com.binarfinalproject.rajawali.dto.user.response.ResUserDto;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.UserService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
            Specification<User> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });

            Page<ResUserDto> response = userService.getAllUsers(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplanes has successfully fetched!",
                    response);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{userId}/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getProfile(@PathVariable UUID userId) {
        try {
            ResProfileDto response = userService.findProfileByUserId(userId);

            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Profile has successfully fetched!", response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> updateProfile(
            @Valid @RequestBody UpdateProfileDto request) {
        try {
            ResProfileDto response = userService.updateProfile(request);

            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Profile has successfully updated!", response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
