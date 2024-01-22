package com.binarfinalproject.rajawali.entity;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE id = ?")
public class Payment extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;

    private String method;

    @Column(name = "receiver_number")
    private String receiverNumber;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
    
    @Column(name = "is_paid")
    private Boolean isPaid;
}
