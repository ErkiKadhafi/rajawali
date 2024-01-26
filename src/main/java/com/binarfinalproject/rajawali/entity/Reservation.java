package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
@SQLDelete(sql = "UPDATE reservations SET is_deleted = true WHERE id = ?")
public class Reservation extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public enum GenderType {
        MAN, WOMAN
    }

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    private String fullname;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private double totalPrice;
}