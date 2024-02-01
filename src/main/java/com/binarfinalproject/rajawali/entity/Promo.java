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
@Table(name = "promos")
@SQLDelete(sql = "UPDATE promos SET is_deleted = true WHERE id = ?")
public class Promo extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;

    @Column(name = "discount_percentage")
    private Double discountPercentage;

    private String description;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
}
