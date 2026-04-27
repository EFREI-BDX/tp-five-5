package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.ActiveFieldEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository exposing the active field read model used by availability.
 */
@Repository
interface ActiveFieldRepository : JpaRepository<ActiveFieldEntity, String> {
    /**
     * Returns active field candidates sorted by name.
     */
    fun findAllByOrderByNameAsc(): List<ActiveFieldEntity>
}
