package com.binarfinalproject.rajawali.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@SQLDelete(sql = "UPDATE meals SET is_deleted = true WHERE id = ?")
public class Role extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    private String name;
}
