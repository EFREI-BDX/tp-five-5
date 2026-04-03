package com.jad.efreifive.managefield.service;

import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.repository.ReservationEntityRepository;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationEntityRepository reservationEntityRepository;

    @Transactional
    public ReservationEntity createReservation(Id fieldId, Id statusId, TimeSlot timeSlot) {
        return reservationEntityRepository.create(fieldId, statusId, timeSlot);
    }

    @Transactional
    public ReservationEntity updateReservationStatus(Id fieldId, Id reservationId, Id statusId) {
        return reservationEntityRepository.updateStatus(fieldId, reservationId, statusId);
    }
}
