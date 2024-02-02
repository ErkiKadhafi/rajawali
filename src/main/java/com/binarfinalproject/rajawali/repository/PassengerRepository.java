package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Passenger;
import java.util.List;

@Repository
public interface PassengerRepository extends SoftDeleteRepository<Passenger, UUID> {
    List<Passenger> findByReservationDetailsId(UUID id);

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.reservationDetails.id = ?1 group by e.id")
    List<Passenger> findPassengersGroupedByReservationDetailsId(UUID reservationDetailsId);
}
