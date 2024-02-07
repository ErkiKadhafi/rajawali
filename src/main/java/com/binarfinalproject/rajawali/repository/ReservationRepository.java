package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Reservation;

@Repository
public interface ReservationRepository extends SoftDeleteRepository<Reservation, UUID> {
    @Query("select count(e) from #{#entityName} e where e.isDeleted = false and e.user.id = ?1")
    long countByUserId(UUID userId);
}
