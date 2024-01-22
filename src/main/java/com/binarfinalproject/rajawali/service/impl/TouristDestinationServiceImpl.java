package com.binarfinalproject.rajawali.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.tourist_destination.response.ResTouristDestinationDto;
import com.binarfinalproject.rajawali.entity.TouristDestination;
import com.binarfinalproject.rajawali.repository.TouristDestinationRepository;
import com.binarfinalproject.rajawali.service.TouristDestinationService;

@Service
public class TouristDestinationServiceImpl implements TouristDestinationService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TouristDestinationRepository touristDestinationRepository;

    @Override
    public Page<ResTouristDestinationDto> getAllTouristDestinations(Specification<TouristDestination> filterQueries,
            Pageable paginationQueries) {
        Page<TouristDestination> touristDestinations = touristDestinationRepository.findAll(filterQueries,
                paginationQueries);

        return touristDestinations.map(d -> modelMapper.map(d, ResTouristDestinationDto.class));
    }

}
