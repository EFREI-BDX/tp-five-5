package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;

import java.util.Optional;

public interface IFieldDomainService {
    Optional<Field> getFieldById(Id id);

    Boolean createReservation(Id id, Period period);

}
