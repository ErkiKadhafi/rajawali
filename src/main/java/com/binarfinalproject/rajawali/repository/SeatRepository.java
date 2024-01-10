package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

}
