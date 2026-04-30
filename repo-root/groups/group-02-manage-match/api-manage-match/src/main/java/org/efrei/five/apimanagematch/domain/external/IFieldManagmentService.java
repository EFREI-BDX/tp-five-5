package org.efrei.five.apimanagematch.domain.external;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;

import java.util.Optional;

public interface IFieldManagmentService {
    Optional<Field> getField(Id fieldId);

    Boolean createReservation(Id fieldId, Period period);
}
