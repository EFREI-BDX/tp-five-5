package fr.efrei.managefield.domain.valueobject

import java.util.Objects
import java.util.UUID

/**
 * Value object wrapping UUID identifiers used by the Field Management domain.
 */
@ConsistentCopyVisibility
data class DomainId private constructor(val value: String) {
    companion object {
        /**
         * Creates a domain identifier from a raw string.
         *
         * @param raw raw identifier received from an external layer
         * @throws IllegalArgumentException when the value is blank or not a UUID
         */
        fun from(raw: String): DomainId {
            val value = Objects.requireNonNull(raw, "id must not be null").trim()
            require(value.isNotEmpty()) { "id is required" }

            val uuid = try {
                UUID.fromString(value)
            } catch (exception: IllegalArgumentException) {
                throw IllegalArgumentException("id must be a valid UUID", exception)
            }

            return DomainId(uuid.toString())
        }
    }
}
