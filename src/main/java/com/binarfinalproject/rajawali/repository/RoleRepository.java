package com.binarfinalproject.rajawali.repository;

import com.binarfinalproject.rajawali.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Role.ERole name);
}