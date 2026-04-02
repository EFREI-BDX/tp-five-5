package com.jad.efreifive.managefield.config;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.repository.FieldStatusEntityRepository;
import com.jad.efreifive.managefield.repository.ReservationStatusEntityRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReferenceDataSeeder implements ApplicationRunner {

    private final FieldStatusEntityRepository fieldStatusEntityRepository;
    private final ReservationStatusEntityRepository reservationStatusEntityRepository;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        ReferenceDataCatalog.fieldStatuses().forEach(this::upsertFieldStatus);
        ReferenceDataCatalog.reservationStatuses().forEach(this::upsertReservationStatus);
    }

    private void upsertFieldStatus(FieldStatusEntity seededStatus) {
        fieldStatusEntityRepository.findByCode(seededStatus.getCode())
            .map(existingStatus -> {
                existingStatus.setLabel(seededStatus.getLabel());
                return fieldStatusEntityRepository.save(existingStatus);
            })
            .orElseGet(() -> fieldStatusEntityRepository.save(seededStatus));
    }

    private void upsertReservationStatus(ReservationStatusEntity seededStatus) {
        reservationStatusEntityRepository.findByCode(seededStatus.getCode())
            .map(existingStatus -> {
                existingStatus.setLabel(seededStatus.getLabel());
                return reservationStatusEntityRepository.save(existingStatus);
            })
            .orElseGet(() -> reservationStatusEntityRepository.save(seededStatus));
    }
}
