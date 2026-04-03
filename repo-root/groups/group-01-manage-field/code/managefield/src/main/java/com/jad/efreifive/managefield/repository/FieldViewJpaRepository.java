package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.persistence.FieldViewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldViewJpaRepository extends JpaRepository<FieldViewRecord, String> {
}
