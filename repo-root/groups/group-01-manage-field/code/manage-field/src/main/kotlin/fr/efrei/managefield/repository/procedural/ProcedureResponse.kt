package fr.efrei.managefield.repository.procedural

/**
 * Base projection returned by stored procedures exposing SQL-domain status
 * metadata. SQL codes are not HTTP status codes.
 */
interface ProcedureResponse {
    /**
     * Returns the SQL-domain status code produced by the procedure.
     */
    fun getSqlCode(): Int

    /**
     * Returns the human-readable SQL-domain status message.
     */
    fun getSqlMessage(): String
}

/**
 * Procedure response containing only status metadata.
 */
interface SimpleProcedureResult : ProcedureResponse

/**
 * Procedure response returned by `app_create_field`.
 */
interface CreateFieldProcedureResult : ProcedureResponse {
    /**
     * Returns the created field identifier when the procedure succeeds.
     */
    fun getFieldId(): String?
}

/**
 * Procedure response returned by `app_create_reservation`.
 */
interface CreateReservationProcedureResult : ProcedureResponse {
    /**
     * Returns the created reservation identifier when the procedure succeeds.
     */
    fun getReservationId(): String?
}
