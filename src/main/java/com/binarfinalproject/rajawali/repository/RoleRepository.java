package com.binarfinalproject.rajawali.repository;

import com.binarfinalproject.rajawali.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends SoftDeleteRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
