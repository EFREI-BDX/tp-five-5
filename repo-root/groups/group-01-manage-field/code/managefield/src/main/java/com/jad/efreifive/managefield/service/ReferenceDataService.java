package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.exception.NotFoundException;
import com.jad.efreifive.managefield.repository.FieldStatusEntityRepository;
import com.jad.efreifive.managefield.repository.ReservationStatusEntityRepository;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceDataService {

    private final FieldStatusEntityRepository fieldStatusEntityRepository;
    private final ReservationStatusEntityRepository reservationStatusEntityRepository;

    public List<FieldStatusEntity> listFieldStatuses() {
        return fieldStatusEntityRepository.findAll().stream()
            .sorted(Comparator.comparing(status -> orderFieldStatus(status.getCode())))
            .toList();
    }

    public List<ReservationStatusEntity> listReservationStatuses() {
        return reservationStatusEntityRepository.findAll().stream()
            .sorted(Comparator.comparing(status -> orderReservationStatus(status.getCode())))
            .toList();
    }

    public FieldStatusEntity getFieldStatus(Id statusId) {
        return fieldStatusEntityRepository.findById(statusId)
            .orElseThrow(() -> new NotFoundException("Field status not found for id " + statusId.asString() + "."));
    }

    public ReservationStatusEntity getReservationStatus(Id statusId) {
        return reservationStatusEntityRepository.findById(statusId)
            .orElseThrow(() -> new NotFoundException("Reservation status not found for id " + statusId.asString() + "."));
    }

    public Id getFieldStatusIdByCode(String code) {
        return fieldStatusEntityRepository.findByCode(FieldStatusCode.of(code))
            .map(FieldStatusEntity::getId)
            .orElseThrow(() -> new NotFoundException("Field status not found for code " + code + "."));
    }

    public List<Id> getReservationStatusIdsByCodes(String... codes) {
        return List.of(codes).stream()
            .map(ReservationStatusCode::of)
            .map(statusCode -> reservationStatusEntityRepository.findByCode(statusCode)
                .map(ReservationStatusEntity::getId)
                .orElseThrow(() -> new NotFoundException("Reservation status not found for code " + statusCode.value() + ".")))
            .toList();
    }

    private int orderFieldStatus(FieldStatusCode fieldStatusCode) {
        return switch (fieldStatusCode.value()) {
            case FieldStatusCode.ACTIVE -> 0;
            case FieldStatusCode.INACTIVE -> 1;
            case FieldStatusCode.MAINTENANCE -> 2;
            default -> Integer.MAX_VALUE;
        };
    }

    private int orderReservationStatus(ReservationStatusCode reservationStatusCode) {
        return switch (reservationStatusCode.value()) {
            case ReservationStatusCode.PENDING -> 0;
            case ReservationStatusCode.CONFIRMED -> 1;
            case ReservationStatusCode.CANCELLED -> 2;
            default -> Integer.MAX_VALUE;
        };
    }
}
