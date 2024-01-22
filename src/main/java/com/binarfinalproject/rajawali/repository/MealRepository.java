package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import com.binarfinalproject.rajawali.entity.Meal;
import java.util.Optional;

public interface MealRepository extends SoftDeleteRepository<Meal, UUID> {
    Optional<Meal> findByName(String name);
}
