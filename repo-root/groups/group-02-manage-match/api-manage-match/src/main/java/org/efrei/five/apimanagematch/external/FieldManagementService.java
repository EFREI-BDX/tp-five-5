package org.efrei.five.apimanagematch.external;

import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.external.IFieldManagementService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldManagementService implements IFieldManagementService {

    private final FieldManagementClient fieldManagementClient;

    public FieldManagementService(FieldManagementClient fieldManagementClient) {
        this.fieldManagementClient = fieldManagementClient;
    }

    @Override
    public Optional<Field> getField(Id fieldId) {
        return fieldManagementClient.getField(fieldId);
    }

    @Override
    public Boolean createReservation(Id fieldId, Period period) {
        return fieldManagementClient.createReservation(fieldId, period);
    }
}
