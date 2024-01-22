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
@Table(name = "tourist_destinations")
@SQLDelete(sql = "UPDATE tourist_destinations SET is_deleted = true WHERE id = ?")
public class TouristDestination extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "source_city")
    private String sourceCity;

    @Column(name = "source_city_code")
    private String sourceCityCode;

    @Column(name = "destination_city")
    private String destinationCity;

    @Column(name = "destination_city_code")
    private String destinationCityCode;

    @Column(name = "start_from_price")
    private double startFromPrice;
}
