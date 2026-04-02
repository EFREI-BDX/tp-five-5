package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.vo.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationEntityRepository extends JpaRepository<ReservationEntity, Id> {

    Optional<ReservationEntity> findByIdAndFieldId(Id id, Id fieldId);
}
