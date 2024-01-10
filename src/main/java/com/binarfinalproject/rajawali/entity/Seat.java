package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
@SQLDelete(sql = "UPDATE seats SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted=false")
public class Seat extends AuditModel {

    public enum ClassType {
        ECONOMY, BUSINESS, FIRST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String seatNo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Enumerated(EnumType.STRING)
    private ClassType classType;
}