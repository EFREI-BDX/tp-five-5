package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.repository.FieldStatusEntityRepository;
import com.jad.efreifive.managefield.repository.ReservationStatusEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceDataService {

    private final FieldStatusEntityRepository fieldStatusEntityRepository;
    private final ReservationStatusEntityRepository reservationStatusEntityRepository;

    public List<FieldStatusEntity> listFieldStatuses() {
        return fieldStatusEntityRepository.findAll();
    }

    public List<ReservationStatusEntity> listReservationStatuses() {
        return reservationStatusEntityRepository.findAll();
    }
}
