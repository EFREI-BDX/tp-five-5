package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.ReservationStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository exposing read access to reservation status view projections.
 */
@Repository
interface ReservationStatusRepository : JpaRepository<ReservationStatusEntity, String> {
    /**
     * Returns all reservation statuses sorted by code.
     */
    fun findAllByOrderByCodeAsc(): List<ReservationStatusEntity>

}
