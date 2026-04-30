package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.external.IFieldManagmentService;
import org.efrei.five.apimanagematch.domain.external.IFieldRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldDomaineService implements IFieldDomainService {

    private final IFieldManagmentService fieldManagmentService;
    private final IFieldRepository repository;

    public FieldDomaineService(IFieldManagmentService fieldManagmentService, IFieldRepository repository) {
        this.fieldManagmentService = fieldManagmentService;
        this.repository = repository;
    }

    @Override
    public Optional<Field> getFieldById(Id id) {
        fieldManagmentService.getField(id).ifPresent(repository::upsert);
        return repository.findById(id);
    }

    @Override
    public Boolean createReservation(Id id, Period period) {
        return fieldManagmentService.createReservation(id, period);
    }
}
