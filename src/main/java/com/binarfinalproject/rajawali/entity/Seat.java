package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
public class Seat {

    public enum ClassType {
        ECONOMY, BUSINESS, FIRST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @OneToOne(mappedBy = "seat")
    private Reservation reservation;
}