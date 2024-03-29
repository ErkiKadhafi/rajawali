package com.binarfinalproject.rajawali.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.binarfinalproject.rajawali.entity.auditModel.AuditModel;

@NoRepositoryBean
public interface SoftDeleteRepository<T extends AuditModel, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    @Override
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.id = ?1")
    Optional<T> findById(ID id);

    @Query("select e from #{#entityName} e where e.isDeleted = true and e.id = ?1")
    Optional<T> findByIdDeleted(ID id);
}
