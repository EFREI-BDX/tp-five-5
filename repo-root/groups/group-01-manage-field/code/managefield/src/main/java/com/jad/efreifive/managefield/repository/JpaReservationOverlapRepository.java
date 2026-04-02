package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class JpaReservationOverlapRepository implements ReservationOverlapRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsActiveOverlap(Id fieldId, TimeSlot timeSlot, Collection<Id> blockingReservationStatusIds, Id excludedReservationId) {
        Long overlapCount = entityManager.createQuery(
                """
                select count(reservation.id)
                from ReservationEntity reservation
                where reservation.fieldId = :fieldId
                  and reservation.timeSlot.date = :reservationDate
                  and reservation.statusId in :blockingReservationStatusIds
                  and reservation.timeSlot.startTime < :requestedEndTime
                  and reservation.timeSlot.endTime > :requestedStartTime
                  and (:excludedReservationId is null or reservation.id <> :excludedReservationId)
                """,
                Long.class
            )
            .setParameter("fieldId", fieldId)
            .setParameter("reservationDate", timeSlot.getDate())
            .setParameter("requestedStartTime", timeSlot.getStartTime())
            .setParameter("requestedEndTime", timeSlot.getEndTime())
            .setParameter("blockingReservationStatusIds", blockingReservationStatusIds)
            .setParameter("excludedReservationId", excludedReservationId)
            .getSingleResult();
        return overlapCount != null && overlapCount > 0;
    }
}
