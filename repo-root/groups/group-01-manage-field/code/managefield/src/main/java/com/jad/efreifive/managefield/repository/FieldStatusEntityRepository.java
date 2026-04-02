package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldStatusEntityRepository extends JpaRepository<FieldStatusEntity, Id> {

    Optional<FieldStatusEntity> findByCode(FieldStatusCode code);
}
