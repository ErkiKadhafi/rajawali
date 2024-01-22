package com.binarfinalproject.rajawali.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binarfinalproject.rajawali.dto.meal.response.ResMealDto;
import com.binarfinalproject.rajawali.entity.Meal;
import com.binarfinalproject.rajawali.service.MealService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.persistence.criteria.Predicate;

@Controller
@RequestMapping("/v1/meals")
public class MealController {
    @Autowired
    MealService mealService;

    @GetMapping
    public ResponseEntity<Object> getAllMeals(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            if (page == null)
                page = 0;
            if (pageSize == null)
                pageSize = 10;

            Pageable paginationQueries = PageRequest.of(page, pageSize);
            Specification<Meal> filterQueries = ((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            });

            Page<ResMealDto> response = mealService.getAllMeals(filterQueries, paginationQueries);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Airplanes has successfully fetched!",
                    response);
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
