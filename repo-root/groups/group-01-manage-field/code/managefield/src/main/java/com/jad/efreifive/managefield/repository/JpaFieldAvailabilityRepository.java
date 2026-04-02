package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class JpaFieldAvailabilityRepository implements FieldAvailabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FieldEntity> findAvailableFields(TimeSlot timeSlot, Id activeFieldStatusId, Collection<Id> blockingReservationStatusIds) {
        return entityManager.createQuery(
                """
                select field
                from FieldEntity field
                where field.statusId = :activeFieldStatusId
                  and not exists (
                    select reservation.id
                    from ReservationEntity reservation
                    where reservation.fieldId = field.id
                      and reservation.timeSlot.date = :reservationDate
                      and reservation.statusId in :blockingReservationStatusIds
                      and reservation.timeSlot.startTime < :requestedEndTime
                      and reservation.timeSlot.endTime > :requestedStartTime
                  )
                order by field.name
                """,
                FieldEntity.class
            )
            .setParameter("activeFieldStatusId", activeFieldStatusId)
            .setParameter("reservationDate", timeSlot.getDate())
            .setParameter("requestedStartTime", timeSlot.getStartTime())
            .setParameter("requestedEndTime", timeSlot.getEndTime())
            .setParameter("blockingReservationStatusIds", blockingReservationStatusIds)
            .getResultList();
    }
}
