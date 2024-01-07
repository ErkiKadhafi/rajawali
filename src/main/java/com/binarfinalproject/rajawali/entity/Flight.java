package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "source_airport_id")
    private Airport sourceAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id")
    private Airport destinationAirport;

    // butuh entity Airplane, jangan lupa
    @ManyToOne
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private double economySeatsPrice;

    private double businessSeatsPrice;

    private double firstSeatsPrice;

    private double discount;

    @OneToMany(mappedBy = "flight")
    private List<Seat> seats;
}