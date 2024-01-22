package com.binarfinalproject.rajawali.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.binarfinalproject.rajawali.dto.meal.response.ResMealDto;
import com.binarfinalproject.rajawali.entity.Meal;

public interface MealService {
    public Page<ResMealDto> getAllMeals(Specification<Meal> filterQueries, Pageable paginationQueries);
}
