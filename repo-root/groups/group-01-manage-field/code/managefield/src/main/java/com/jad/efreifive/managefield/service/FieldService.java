package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.exception.NotFoundException;
import com.jad.efreifive.managefield.repository.FieldAvailabilityRepository;
import com.jad.efreifive.managefield.repository.FieldEntityRepository;
import com.jad.efreifive.managefield.vo.Id;
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

    @Transactional(readOnly = true)
    public List<FieldEntity> listAvailableFields(TimeSlot requestedTimeSlot) {
        return fieldAvailabilityRepository.findAvailableFields(requestedTimeSlot);
    }

    @Transactional(readOnly = true)
    public FieldEntity getField(Id fieldId) {
        return fieldEntityRepository.findById(fieldId)
            .orElseThrow(() -> new NotFoundException("Field not found for id " + fieldId.asString() + "."));
    }

    @Transactional
    public FieldEntity updateFieldStatus(Id fieldId, Id statusId) {
        return fieldEntityRepository.updateStatus(fieldId, statusId);
    }
}
