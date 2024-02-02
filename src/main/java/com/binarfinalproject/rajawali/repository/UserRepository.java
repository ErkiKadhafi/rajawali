package com.binarfinalproject.rajawali.repository;

import com.binarfinalproject.rajawali.entity.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends SoftDeleteRepository<User, UUID> {
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select case when count(e)> 0 then true else false end from #{#entityName} e where e.isDeleted = false and e.phoneNumber = ?1")
    boolean existByPhoneNumber(String phoneNumber);
}
