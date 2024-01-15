package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Passenger;
import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID>, JpaSpecificationExecutor<Passenger> {
    List<Passenger> findByReservationDetailsId(UUID id);
}
