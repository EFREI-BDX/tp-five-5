package fr.efrei.managefield.service.port

import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.FieldName

/**
 * Application port for field write operations.
 */
interface FieldWritePort {
    /**
     * Creates a field and returns its identifier.
     */
    fun createField(fieldId: DomainId?, name: FieldName, status: FieldStatusCode): DomainId

    /**
     * Changes the status of an existing field.
     */
    fun changeFieldStatus(fieldId: DomainId, status: FieldStatusCode)
}
