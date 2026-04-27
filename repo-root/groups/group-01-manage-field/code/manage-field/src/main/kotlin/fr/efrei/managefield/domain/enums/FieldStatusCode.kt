package fr.efrei.managefield.domain.enums

/**
 * Enumerates fixed field statuses stored as reference data in MariaDB.
 */
enum class FieldStatusCode(val id: String, val code: String) {
    ACTIVE("11111111-1111-4111-8111-111111111111", "active"),
    INACTIVE("11111111-1111-4111-8111-111111111112", "inactive"),
    MAINTENANCE("11111111-1111-4111-8111-111111111113", "maintenance");

    companion object {
        /**
         * Resolves a database code to an enum value.
         */
        fun fromCode(code: String): FieldStatusCode? {
            return entries.find { it.code == code.trim().lowercase() }
        }

        /**
         * Resolves a fixed database identifier to an enum value.
         */
        fun fromId(id: String): FieldStatusCode? {
            return entries.find { it.id == id.trim().lowercase() }
        }
    }
}
