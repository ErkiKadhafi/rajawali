package com.binarfinalproject.rajawali.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.TouristDestination;

@Repository
public interface TouristDestinationRepository extends SoftDeleteRepository<TouristDestination, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.sourceCityCode = ?1 and e.destinationCityCode = ?2")
    Optional<TouristDestination> findBySourceCityCodeAndDestinationCityCode(String sourceCityCode,
            String destinationCityCode);
}
