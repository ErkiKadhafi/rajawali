package com.binarfinalproject.rajawali.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.meal.response.ResMealDto;
import com.binarfinalproject.rajawali.entity.Meal;
import com.binarfinalproject.rajawali.repository.MealRepository;
import com.binarfinalproject.rajawali.service.MealService;

@Service
public class MealServiceImpl implements MealService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MealRepository mealRepository;

    @Override
    public Page<ResMealDto> getAllMeals(Specification<Meal> filterQueries, Pageable paginationQueries) {
        Page<Meal> meals = mealRepository.findAll(filterQueries, paginationQueries);
        Page<ResMealDto> resMealDto = meals
                .map(productEntity -> modelMapper.map(productEntity, ResMealDto.class));
        return resMealDto;
    }

}
