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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private boolean isAdmin;

    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}
