package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;

import java.util.Collection;

public interface ReservationOverlapRepository {

    boolean existsActiveOverlap(Id fieldId, TimeSlot timeSlot, Collection<Id> blockingReservationStatusIds, Id excludedReservationId);
}
