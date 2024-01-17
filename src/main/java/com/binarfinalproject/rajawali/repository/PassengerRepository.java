package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Passenger;
import java.util.List;

@Repository
public interface PassengerRepository extends SoftDeleteRepository<Passenger, UUID> {
    List<Passenger> findByReservationDetailsId(UUID id);
}
