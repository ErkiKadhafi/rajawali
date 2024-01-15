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
@Table(name = "contact_details")
@SQLDelete(sql = "UPDATE contact_details SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted=false")
public class ContactDetails extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "contactDetails")
    private Reservation reservation;

    public enum GenderType {
        MAN, WOMAN
    }

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    private String fullname;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
}
