package fr.efrei.managefield.service.port

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.valueobject.DomainId

/**
 * Application port for field read operations.
 */
interface FieldReadPort {
    /**
     * Finds a field aggregate by identifier.
     */
    fun findById(fieldId: DomainId): Field?

    /**
     * Lists fields already constrained to active persistence read models.
     */
    fun listActiveFields(): List<Field>
}
