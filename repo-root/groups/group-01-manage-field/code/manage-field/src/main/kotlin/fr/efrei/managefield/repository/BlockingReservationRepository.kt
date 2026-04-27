package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.BlockingReservationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * Repository exposing reservations that block availability checks.
 */
@Repository
interface BlockingReservationRepository : JpaRepository<BlockingReservationEntity, String> {
    /**
     * Lists pending or confirmed reservations for one day.
     */
    fun findAllByDate(date: LocalDate): List<BlockingReservationEntity>
}
