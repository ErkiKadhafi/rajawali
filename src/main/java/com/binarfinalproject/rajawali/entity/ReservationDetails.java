package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation_details")
@SQLDelete(sql = "UPDATE reservation_details SET is_deleted = true WHERE id = ?")
public class ReservationDetails extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @OneToMany(mappedBy = "reservationDetails", fetch = FetchType.LAZY)
    private List<Passenger> passengers;

    @Column(name = "use_travel_assurance")
    private boolean useTravelAssurance;

    @Column(name = "use_bagage_assurance")
    private boolean useBagageAssurance;

    @Column(name = "use_flight_delay_assurance")
    private boolean useFlightDelayAssurance;

    @Column(name = "seat_price")
    private double seatPrice;

    @Column(name = "total_price")
    private double totalPrice;
}
