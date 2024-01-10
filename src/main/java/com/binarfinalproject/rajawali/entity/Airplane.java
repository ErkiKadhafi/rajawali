package com.binarfinalproject.rajawali.entity;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "airplanes")
@SQLDelete(sql = "UPDATE airplanes SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted=false")
public class Airplane extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "airplane_code")
    private String airplaneCode;

    @Column(name = "economy_seats")
    private Integer economySeats;

    @Column(name = "business_Seats")
    private Integer businessSeats;

    @Column(name = "first_seats")
    private Integer firstSeats;

    @Column(name = "economy_seats_per_col")
    private Integer economySeatsPerCol;

    @Column(name = "business_seats_per_col")
    private Integer businessSeatsPerCol;

    @Column(name = "first_seats_per_col")
    private Integer firstSeatsPerCol;

    @OneToMany(mappedBy = "airplane", fetch = FetchType.LAZY)
    private List<Seat> seats;
}
