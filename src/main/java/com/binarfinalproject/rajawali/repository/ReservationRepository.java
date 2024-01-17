package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Reservation;

@Repository
public interface ReservationRepository extends SoftDeleteRepository<Reservation, UUID> {

}
