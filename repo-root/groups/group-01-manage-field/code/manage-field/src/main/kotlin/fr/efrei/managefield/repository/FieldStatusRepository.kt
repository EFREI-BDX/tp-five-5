package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.FieldStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository exposing read access to field status view projections.
 */
@Repository
interface FieldStatusRepository : JpaRepository<FieldStatusEntity, String> {
    /**
     * Returns all field statuses sorted by code.
     */
    fun findAllByOrderByCodeAsc(): List<FieldStatusEntity>

}
