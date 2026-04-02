package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.exception.ConflictException;
import com.jad.efreifive.managefield.exception.NotFoundException;
import com.jad.efreifive.managefield.repository.FieldEntityRepository;
import com.jad.efreifive.managefield.repository.ReservationEntityRepository;
import com.jad.efreifive.managefield.repository.ReservationOverlapRepository;
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
public class ReservationService {

    private final FieldEntityRepository fieldEntityRepository;
    private final ReservationEntityRepository reservationEntityRepository;
    private final ReservationOverlapRepository reservationOverlapRepository;
    private final ReferenceDataService referenceDataService;

    @Transactional
    public ReservationEntity createReservation(Id fieldId, Id statusId, TimeSlot timeSlot) {
        FieldEntity field = getField(fieldId);
        ensureFieldIsActive(field);

        ReservationStatusEntity reservationStatus = referenceDataService.getReservationStatus(statusId);
        ensureNoOverlapIfBlocking(fieldId, timeSlot, reservationStatus.getCode(), null);

        ReservationEntity reservation = ReservationEntity.builder()
            .id(Id.random())
            .fieldId(fieldId)
            .statusId(statusId)
            .timeSlot(timeSlot)
            .build();

        return reservationEntityRepository.save(reservation);
    }

    @Transactional
    public ReservationEntity updateReservationStatus(Id fieldId, Id reservationId, Id statusId) {
        ReservationEntity reservation = reservationEntityRepository.findByIdAndFieldId(reservationId, fieldId)
            .orElseThrow(() -> new NotFoundException(
                "Reservation not found for id " + reservationId.asString() + " on field " + fieldId.asString() + "."
            ));

        ReservationStatusEntity reservationStatus = referenceDataService.getReservationStatus(statusId);
        if (reservationStatus.getCode().isBlocking()) {
            ensureFieldIsActive(getField(fieldId));
        }
        ensureNoOverlapIfBlocking(fieldId, reservation.getTimeSlot(), reservationStatus.getCode(), reservationId);

        reservation.setStatusId(statusId);
        return reservationEntityRepository.save(reservation);
    }

    private FieldEntity getField(Id fieldId) {
        return fieldEntityRepository.findById(fieldId)
            .orElseThrow(() -> new NotFoundException("Field not found for id " + fieldId.asString() + "."));
    }

    private void ensureFieldIsActive(FieldEntity field) {
        Id activeFieldStatusId = referenceDataService.getFieldStatusIdByCode(FieldStatusCode.ACTIVE);
        if (!activeFieldStatusId.equals(field.getStatusId())) {
            throw new ConflictException("Field " + field.getId().asString() + " is not active and cannot be reserved.");
        }
    }

    private void ensureNoOverlapIfBlocking(Id fieldId, TimeSlot timeSlot, ReservationStatusCode reservationStatusCode, Id excludedReservationId) {
        if (!reservationStatusCode.isBlocking()) {
            return;
        }
        List<Id> blockingStatusIds = referenceDataService.getReservationStatusIdsByCodes(
            ReservationStatusCode.PENDING,
            ReservationStatusCode.CONFIRMED
        );
        boolean overlapExists = reservationOverlapRepository.existsActiveOverlap(
            fieldId,
            timeSlot,
            blockingStatusIds,
            excludedReservationId
        );
        if (overlapExists) {
            throw new ConflictException("Another active reservation already overlaps this time slot for field " + fieldId.asString() + ".");
        }
    }
}
