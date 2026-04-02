package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;

public interface ReservationEntityRepository {

    ReservationEntity create(Id fieldId, Id statusId, TimeSlot timeSlot);

    ReservationEntity updateStatus(Id fieldId, Id reservationId, Id statusId);
}
