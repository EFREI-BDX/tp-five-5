package fr.efrei.managefield.domain.valueobject

import java.util.Objects

/**
 * Value object representing the business name of a reservable field.
 */
@ConsistentCopyVisibility
data class FieldName private constructor(val value: String) {
    companion object {
        /**
         * Creates a field name from a raw string.
         *
         * @param raw raw field name
         * @throws IllegalArgumentException when the name is blank
         */
        fun from(raw: String): FieldName {
            val value = Objects.requireNonNull(raw, "field name must not be null").trim()
            require(value.isNotEmpty()) { "field name is required" }
            return FieldName(value)
        }
    }
}
