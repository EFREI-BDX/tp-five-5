package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.persistence.FieldViewRecord;
import com.jad.efreifive.managefield.vo.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaFieldEntityRepository implements FieldEntityRepository {

    private final FieldViewJpaRepository fieldViewJpaRepository;
    private final NamedStoredProcedureExecutor storedProcedureExecutor;

    @Override
    public Optional<FieldEntity> findById(Id id) {
        return fieldViewJpaRepository.findById(id.asString())
            .map(PersistenceEntityMapper::toDomain);
    }

    @Override
    public FieldEntity updateStatus(Id fieldId, Id statusId) {
        return storedProcedureExecutor.queryForSingleResult(
            FieldViewRecord.UPDATE_STATUS_PROCEDURE,
            query -> {
                query.setParameter("p_field_id", fieldId.asString());
                query.setParameter("p_status_id", statusId.asString());
            },
            result -> PersistenceEntityMapper.toDomain((FieldViewRecord) result)
        );
    }
}
