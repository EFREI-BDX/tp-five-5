package fr.efrei.managefield.domain.entity

import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.FieldName
import java.util.Objects

/**
 * Domain aggregate representing a reservable field.
 */
@ConsistentCopyVisibility
data class Field private constructor(
    val id: DomainId,
    val name: FieldName,
    val status: FieldStatusCode
) {
    companion object {
        /**
         * Restores a field aggregate from trusted persistence data.
         */
        fun restore(id: DomainId, name: FieldName, status: FieldStatusCode): Field {
            return Field(
                id = Objects.requireNonNull(id, "field id must not be null"),
                name = Objects.requireNonNull(name, "field name must not be null"),
                status = Objects.requireNonNull(status, "field status must not be null")
            )
        }
    }

    /**
     * Returns true when the field can receive blocking reservations.
     */
    fun isReservable(): Boolean {
        return status == FieldStatusCode.ACTIVE
    }

    /**
     * Ensures the field is reservable.
     */
    fun requireReservable() {
        require(isReservable()) { "field is not active" }
    }

    /**
     * Returns a new field instance with the requested status.
     */
    fun changeStatus(newStatus: FieldStatusCode): Field {
        return copy(status = Objects.requireNonNull(newStatus, "field status must not be null"))
    }
}
