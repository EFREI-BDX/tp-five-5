package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.persistence.BlockingReservationViewRecord;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaReservationEntityRepository implements ReservationEntityRepository {

    private final NamedStoredProcedureExecutor storedProcedureExecutor;

    @Override
    public ReservationEntity create(Id fieldId, Id statusId, TimeSlot timeSlot) {
        return storedProcedureExecutor.queryForSingleResult(
            BlockingReservationViewRecord.CREATE_RESERVATION_PROCEDURE,
            query -> {
                query.setParameter("p_field_id", fieldId.asString());
                query.setParameter("p_status_id", statusId.asString());
                query.setParameter("p_date", timeSlot.getDate());
                query.setParameter("p_start_time", timeSlot.getStartTime());
                query.setParameter("p_end_time", timeSlot.getEndTime());
            },
            result -> PersistenceEntityMapper.toDomain((BlockingReservationViewRecord) result)
        );
    }

    @Override
    public ReservationEntity updateStatus(Id fieldId, Id reservationId, Id statusId) {
        return storedProcedureExecutor.queryForSingleResult(
            BlockingReservationViewRecord.UPDATE_STATUS_PROCEDURE,
            query -> {
                query.setParameter("p_field_id", fieldId.asString());
                query.setParameter("p_reservation_id", reservationId.asString());
                query.setParameter("p_status_id", statusId.asString());
            },
            result -> PersistenceEntityMapper.toDomain((BlockingReservationViewRecord) result)
        );
    }
}
