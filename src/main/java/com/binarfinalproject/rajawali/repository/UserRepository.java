package com.binarfinalproject.rajawali.repository;
import com.binarfinalproject.rajawali.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String Username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findByHandphone(String handphone);
}
