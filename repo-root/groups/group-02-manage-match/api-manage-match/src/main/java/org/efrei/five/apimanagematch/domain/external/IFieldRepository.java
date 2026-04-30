package org.efrei.five.apimanagematch.domain.external;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;

import java.util.Optional;

public interface IFieldRepository {
    void upsert(Field field);

    Optional<Field> findById(Id id);
}
