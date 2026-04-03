package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;

import java.util.List;
import java.util.Optional;

public interface ReservationStatusEntityRepository {

    List<ReservationStatusEntity> findAll();

    Optional<ReservationStatusEntity> findById(Id id);

    Optional<ReservationStatusEntity> findByCode(ReservationStatusCode code);
}
