package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Flight;

@Repository
public interface FlightRepository extends SoftDeleteRepository<Flight, UUID> {

}