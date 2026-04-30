package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.external.IFieldManagmentService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldManagmentService implements IFieldManagmentService {

    private final FieldManagmentClient fieldManagmentClient;

    public FieldManagmentService(FieldManagmentClient fieldManagmentClient) {
        this.fieldManagmentClient = fieldManagmentClient;
    }

    @Override
    public Optional<Field> getField(Id fieldId) {
        return fieldManagmentClient.getField(fieldId);
    }

    @Override
    public Boolean createReservation(Id fieldId, Period period) {
        return fieldManagmentClient.createReservation(fieldId, period);
    }
}
