package com.binarfinalproject.rajawali.repository;

import com.binarfinalproject.rajawali.entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, UUID>, JpaSpecificationExecutor<Airplane> {
}
