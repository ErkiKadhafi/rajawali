package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
@SQLDelete(sql = "UPDATE flights SET is_deleted = true WHERE id = ?")
public class Flight extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "source_airport_id", nullable = false)
    private Airport sourceAirport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destinationAirport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(name = "source_terminal")
    private String sourceTerminal;

    @Column(name = "destination_terminal")
    private String destinationTerminal;

    @Column(name = "departure_date")
    private LocalDateTime departureDate;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;

    @Column(name = "economy_seats_price")
    private double economySeatsPrice;

    @Column(name = "business_seats_price")
    private double businessSeatsPrice;

    @Column(name = "first_seats_price")
    private double firstSeatsPrice;

    private double discount;

    @Column(name = "economy_available_seats")
    private int economyAvailableSeats;

    @Column(name = "business_available_seats")
    private int businessAvailableSeats;

    @Column(name = "first_available_seats")
    private int firstAvailableSeats;

    private int points;
}