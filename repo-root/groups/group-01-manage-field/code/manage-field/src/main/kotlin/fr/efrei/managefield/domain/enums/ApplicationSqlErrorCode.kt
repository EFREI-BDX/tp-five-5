package fr.efrei.managefield.domain.enums

/**
 * Stable SQL procedure return codes. They are deliberately distinct from HTTP
 * status codes and are translated by the application layer.
 */
enum class ApplicationSqlErrorCode(val sqlCode: Int) {
    SUCCESS(0),
    VALIDATION_ERROR(1001),
    RESOURCE_NOT_FOUND(1002),
    STATE_CONFLICT(1003),
    UNEXPECTED_FAILURE(1099);

    companion object {
        /**
         * Resolves a raw SQL procedure code to a known application code.
         */
        fun fromSqlCode(sqlCode: Int): ApplicationSqlErrorCode {
            return entries.find { it.sqlCode == sqlCode } ?: UNEXPECTED_FAILURE
        }
    }
}
