package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.exception.NotFoundException;
import com.jad.efreifive.managefield.repository.FieldAvailabilityRepository;
import com.jad.efreifive.managefield.repository.FieldEntityRepository;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import com.jad.efreifive.managefield.vo.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldEntityRepository fieldEntityRepository;
    private final FieldAvailabilityRepository fieldAvailabilityRepository;
    private final ReferenceDataService referenceDataService;

    @Transactional(readOnly = true)
    public List<FieldEntity> listAvailableFields(TimeSlot requestedTimeSlot) {
        Id activeFieldStatusId = referenceDataService.getFieldStatusIdByCode(FieldStatusCode.ACTIVE);
        List<Id> blockingStatusIds = referenceDataService.getReservationStatusIdsByCodes(
            ReservationStatusCode.PENDING,
            ReservationStatusCode.CONFIRMED
        );
        return fieldAvailabilityRepository.findAvailableFields(requestedTimeSlot, activeFieldStatusId, blockingStatusIds);
    }

    @Transactional(readOnly = true)
    public FieldEntity getField(Id fieldId) {
        return fieldEntityRepository.findById(fieldId)
            .orElseThrow(() -> new NotFoundException("Field not found for id " + fieldId.asString() + "."));
    }

    @Transactional
    public FieldEntity updateFieldStatus(Id fieldId, Id statusId) {
        FieldEntity field = getField(fieldId);
        referenceDataService.getFieldStatus(statusId);
        field.setStatusId(statusId);
        return fieldEntityRepository.save(field);
    }
}
