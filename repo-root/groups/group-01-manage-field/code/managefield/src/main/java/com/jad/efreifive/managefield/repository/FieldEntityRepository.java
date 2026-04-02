package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.vo.FieldName;
import com.jad.efreifive.managefield.vo.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldEntityRepository extends JpaRepository<FieldEntity, Id> {

    Optional<FieldEntity> findByName(FieldName name);
}
