package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.binarfinalproject.rajawali.entity.Seat.ClassType;
import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
@SQLDelete(sql = "UPDATE reservations SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted=false")
public class Reservation extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @OneToOne
    @JoinColumn(name = "contact_details_id", referencedColumnName = "id")
    private ContactDetails contactDetails;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Column(name = "seat_price")
    private double seatPrice;
    
    @Column(name = "total_price")
    private double totalPrice;
}