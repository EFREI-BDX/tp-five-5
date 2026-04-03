package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.vo.TimeSlot;

import java.util.List;

public interface FieldAvailabilityRepository {

    List<FieldEntity> findAvailableFields(TimeSlot timeSlot);
}
