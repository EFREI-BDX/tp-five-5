package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.external.IFieldManagementService;
import org.efrei.five.apimanagematch.domain.external.IFieldRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldDomaineService implements IFieldDomainService {

    private final IFieldManagementService fieldManagementService;
    private final IFieldRepository repository;

    public FieldDomaineService(IFieldManagementService fieldManagementService, IFieldRepository repository) {
        this.fieldManagementService = fieldManagementService;
        this.repository = repository;
    }

    @Override
    public Optional<Field> getFieldById(Id id) {
        fieldManagementService.getField(id).ifPresent(repository::upsert);
        return repository.findById(id);
    }

    @Override
    public Boolean createReservation(Id id, Period period) {
        return fieldManagementService.createReservation(id, period);
    }
}
