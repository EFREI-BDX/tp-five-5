package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationStatusEntityRepository extends JpaRepository<ReservationStatusEntity, Id> {

    Optional<ReservationStatusEntity> findByCode(ReservationStatusCode code);
}
