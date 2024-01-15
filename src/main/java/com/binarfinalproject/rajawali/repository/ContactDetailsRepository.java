package com.binarfinalproject.rajawali.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.binarfinalproject.rajawali.entity.ContactDetails;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails, UUID>, JpaSpecificationExecutor<ContactDetails> {

}
