package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Airport;

@Repository
public interface AirportRepository extends SoftDeleteRepository<Airport, UUID> {

}
