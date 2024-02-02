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
@Table(name = "passengers")
@SQLDelete(sql = "UPDATE passengers SET is_deleted = true WHERE id = ?")
public class Passenger extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_details_id", nullable = false)
    private ReservationDetails reservationDetails;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @OneToMany(mappedBy = "passenger", fetch = FetchType.LAZY)
    private List<PassengerMeal> passengerMeals;

    public enum GenderType {
        MAN, WOMAN
    }

    public enum AgeType {
        ADULT, CHILD, INFANT
    }

    @Column(name = "bagage_add_ons")
    private Integer bagageAddOns;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    private AgeType ageType;

    private String fullname;

    @Column(name = "id_card_number")
    private String idCardNumber;
}
