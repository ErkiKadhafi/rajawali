package com.binarfinalproject.rajawali.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binarfinalproject.rajawali.dto.tourist_destination.response.ResTouristDestinationDto;
import com.binarfinalproject.rajawali.entity.TouristDestination;
import com.binarfinalproject.rajawali.service.TouristDestinationService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.persistence.criteria.Predicate;

@Controller
@RequestMapping("/v1/tourist-destinations")
public class TouristDestinationController {
    @Autowired
    TouristDestinationService touristDestinationService;

    @GetMapping
    public ResponseEntity<Object> getAllTouristDestinations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
            Specification<TouristDestination> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });
            Page<ResTouristDestinationDto> touristDestinations = touristDestinationService
                    .getAllTouristDestinations(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK,
                    "Airports has successfully fetched!", touristDestinations);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
