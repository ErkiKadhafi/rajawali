package com.binarfinalproject.rajawali.entity;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "Airplane")
@SQLDelete(sql = "UPDATE Airplane SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Airplane extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer economy_seats;
    private Integer busines_seats;
    private Integer first_seats;
    private Integer economy_seats_per_col;
    private Integer busines_seats_per_col;
    private Integer first_seats_per_col;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "airplane")
    private List<Flight> flightList;
}
