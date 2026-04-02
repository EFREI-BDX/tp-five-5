package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.persistence.ActiveFieldViewRecord;
import com.jad.efreifive.managefield.persistence.BlockingReservationViewRecord;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaFieldAvailabilityRepository implements FieldAvailabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FieldEntity> findAvailableFields(TimeSlot timeSlot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActiveFieldViewRecord> query = criteriaBuilder.createQuery(ActiveFieldViewRecord.class);
        Root<ActiveFieldViewRecord> field = query.from(ActiveFieldViewRecord.class);

        Subquery<String> overlappingReservations = query.subquery(String.class);
        Root<BlockingReservationViewRecord> reservation = overlappingReservations.from(BlockingReservationViewRecord.class);
        overlappingReservations.select(reservation.get("id"));
        overlappingReservations.where(
            criteriaBuilder.equal(reservation.get("fieldId"), field.get("id")),
            criteriaBuilder.equal(reservation.get("date"), timeSlot.getDate()),
            criteriaBuilder.lessThan(reservation.get("startTime"), timeSlot.getEndTime()),
            criteriaBuilder.greaterThan(reservation.get("endTime"), timeSlot.getStartTime())
        );

        query.select(field)
            .where(criteriaBuilder.not(criteriaBuilder.exists(overlappingReservations)))
            .orderBy(criteriaBuilder.asc(field.get("name")));

        return entityManager.createQuery(query)
            .getResultList()
            .stream()
            .map(PersistenceEntityMapper::toDomain)
            .toList();
    }
}
