package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.persistence.FieldStatusViewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldStatusViewJpaRepository extends JpaRepository<FieldStatusViewRecord, String> {

    List<FieldStatusViewRecord> findAllByOrderBySortOrderAscCodeAsc();

    Optional<FieldStatusViewRecord> findByCode(String code);
}
