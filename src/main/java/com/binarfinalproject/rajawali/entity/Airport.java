package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String city;

    private String country;

    @Column(name = "city_code")
    private String cityCode;

    @OneToMany(mappedBy = "sourceAirport")
    private List<Flight> sourceFlights;

    @OneToMany(mappedBy = "destinationAirport")
    private List<Flight> destinationFlights;
}