package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.ReservationDetails;
import java.util.List;

@Repository
public interface ReservationDetailsRepository extends SoftDeleteRepository<ReservationDetails, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.flight.id = ?1")
    List<ReservationDetails> findByFlightId(UUID id);

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.reservation.id = ?1")
    List<ReservationDetails> findByReservationId(UUID id);
}
