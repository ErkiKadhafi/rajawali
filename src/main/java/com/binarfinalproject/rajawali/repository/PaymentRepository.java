package com.binarfinalproject.rajawali.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.binarfinalproject.rajawali.entity.Payment;

public interface PaymentRepository extends SoftDeleteRepository<Payment, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.reservation.id = ?1")
    Optional<Payment> findByReservationId(UUID reservationId);
}
