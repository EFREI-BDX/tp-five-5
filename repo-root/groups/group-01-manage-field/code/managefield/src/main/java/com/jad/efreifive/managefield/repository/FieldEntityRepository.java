package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.vo.Id;

import java.util.Optional;

public interface FieldEntityRepository {

    Optional<FieldEntity> findById(Id id);

    FieldEntity updateStatus(Id fieldId, Id statusId);
}
