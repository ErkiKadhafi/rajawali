package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Airport;

import java.util.Optional;

@Repository
public interface AirportRepository extends SoftDeleteRepository<Airport, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.name = ?1")
    Optional<Airport> findByName(String name);
}
