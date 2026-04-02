package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;

import java.util.List;
import java.util.Optional;

public interface FieldStatusEntityRepository {

    List<FieldStatusEntity> findAll();

    Optional<FieldStatusEntity> findById(Id id);

    Optional<FieldStatusEntity> findByCode(FieldStatusCode code);
}
