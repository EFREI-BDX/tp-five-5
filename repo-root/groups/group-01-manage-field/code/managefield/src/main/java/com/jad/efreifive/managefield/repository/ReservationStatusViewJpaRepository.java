package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.persistence.ReservationStatusViewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationStatusViewJpaRepository extends JpaRepository<ReservationStatusViewRecord, String> {

    List<ReservationStatusViewRecord> findAllByOrderBySortOrderAscCodeAsc();

    Optional<ReservationStatusViewRecord> findByCode(String code);
}
