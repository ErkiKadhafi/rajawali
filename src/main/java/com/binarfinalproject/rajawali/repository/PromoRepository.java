package com.binarfinalproject.rajawali.repository;

import java.util.Optional;
import java.util.UUID;

import com.binarfinalproject.rajawali.entity.Promo;

public interface PromoRepository extends SoftDeleteRepository<Promo, UUID> {
    Optional<Promo> findByCode(String code);
}
