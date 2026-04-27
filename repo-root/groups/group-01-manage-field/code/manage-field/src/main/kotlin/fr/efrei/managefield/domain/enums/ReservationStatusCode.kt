package fr.efrei.managefield.domain.enums

/**
 * Enumerates fixed reservation statuses stored as reference data in MariaDB.
 */
enum class ReservationStatusCode(val id: String, val code: String) {
    PENDING("33333333-3333-4333-8333-333333333331", "pending"),
    CONFIRMED("33333333-3333-4333-8333-333333333333", "confirmed"),
    CANCELLED("33333333-3333-4333-8333-333333333334", "cancelled");

    companion object {
        /**
         * Resolves a database code to an enum value.
         */
        fun fromCode(code: String): ReservationStatusCode? {
            return entries.find { it.code == code.trim().lowercase() }
        }

        /**
         * Resolves a fixed database identifier to an enum value.
         */
        fun fromId(id: String): ReservationStatusCode? {
            return entries.find { it.id == id.trim().lowercase() }
        }
    }
}
