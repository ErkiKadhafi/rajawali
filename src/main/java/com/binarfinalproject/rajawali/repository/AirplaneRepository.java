package com.binarfinalproject.rajawali.repository;

import com.binarfinalproject.rajawali.entity.Airplane;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirplaneRepository extends SoftDeleteRepository<Airplane, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.airplaneCode = ?1")
    Optional<Airplane> findByAirplaneCode(String airplaneCode);
}
