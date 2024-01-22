package com.binarfinalproject.rajawali.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.binarfinalproject.rajawali.dto.tourist_destination.response.ResTouristDestinationDto;
import com.binarfinalproject.rajawali.entity.TouristDestination;

public interface TouristDestinationService {
    public Page<ResTouristDestinationDto> getAllTouristDestinations(Specification<TouristDestination> filterQueries,
            Pageable paginationQueries);
}
