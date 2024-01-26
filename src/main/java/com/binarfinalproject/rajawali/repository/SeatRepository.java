package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Seat;
import java.util.List;

@Repository
public interface SeatRepository extends SoftDeleteRepository<Seat, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.airplane.id = ?1")
    List<Seat> findByAirplaneId(UUID airplaneId);
    
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.airplane.id = ?1 and e.classType = ?2")
    List<Seat> findByAirplaneIdAndClassType(UUID airplaneId, Seat.ClassType classType);
}
