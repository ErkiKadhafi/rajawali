package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, UUID>, JpaSpecificationExecutor<Airport> {

}
