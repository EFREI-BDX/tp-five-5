package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaReservationStatusEntityRepository implements ReservationStatusEntityRepository {

    private final ReservationStatusViewJpaRepository reservationStatusViewJpaRepository;

    @Override
    public List<ReservationStatusEntity> findAll() {
        return reservationStatusViewJpaRepository.findAllByOrderBySortOrderAscCodeAsc().stream()
            .map(PersistenceEntityMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<ReservationStatusEntity> findById(Id id) {
        return reservationStatusViewJpaRepository.findById(id.asString())
            .map(PersistenceEntityMapper::toDomain);
    }

    @Override
    public Optional<ReservationStatusEntity> findByCode(ReservationStatusCode code) {
        return reservationStatusViewJpaRepository.findByCode(code.value())
            .map(PersistenceEntityMapper::toDomain);
    }
}
