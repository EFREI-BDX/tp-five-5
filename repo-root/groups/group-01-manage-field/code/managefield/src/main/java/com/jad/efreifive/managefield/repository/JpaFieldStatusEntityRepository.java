package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaFieldStatusEntityRepository implements FieldStatusEntityRepository {

    private final FieldStatusViewJpaRepository fieldStatusViewJpaRepository;

    @Override
    public List<FieldStatusEntity> findAll() {
        return fieldStatusViewJpaRepository.findAllByOrderBySortOrderAscCodeAsc().stream()
            .map(PersistenceEntityMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<FieldStatusEntity> findById(Id id) {
        return fieldStatusViewJpaRepository.findById(id.asString())
            .map(PersistenceEntityMapper::toDomain);
    }

    @Override
    public Optional<FieldStatusEntity> findByCode(FieldStatusCode code) {
        return fieldStatusViewJpaRepository.findByCode(code.value())
            .map(PersistenceEntityMapper::toDomain);
    }
}
