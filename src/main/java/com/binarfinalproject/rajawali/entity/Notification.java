package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_deleted = true WHERE id = ?")
public class Notification extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public enum NotificationType {
        AUTHENTICATION, CREATE_RESERVATION, CREATE_PAYMENT, FINISH_PAYMENT, APPROVE_PAYMENT, REJECT_PAYMENT
    }

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String description;
}
