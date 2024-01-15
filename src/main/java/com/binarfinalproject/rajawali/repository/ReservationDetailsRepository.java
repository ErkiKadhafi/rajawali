package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.ReservationDetails;
import java.util.List;

@Repository
public interface ReservationDetailsRepository
        extends JpaRepository<ReservationDetails, UUID>, JpaSpecificationExecutor<ReservationDetails> {

    List<ReservationDetails> findByFlightId(UUID id);
}
