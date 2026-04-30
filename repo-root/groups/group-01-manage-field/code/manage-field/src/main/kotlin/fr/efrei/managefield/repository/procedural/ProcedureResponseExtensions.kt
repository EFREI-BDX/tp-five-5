package fr.efrei.managefield.repository.procedural

import fr.efrei.managefield.service.exception.ApplicationConflictException
import fr.efrei.managefield.service.exception.ApplicationInternalException
import fr.efrei.managefield.service.exception.ApplicationNotFoundException
import fr.efrei.managefield.service.exception.ApplicationValidationException

/**
 * Requires a stored procedure response to be successful and translates SQL
 * return codes into application exceptions.
 */
fun <T : ProcedureResponse> T.requireSuccess(): T {
    return when (ApplicationSqlErrorCode.fromSqlCode(getSqlCode())) {
        ApplicationSqlErrorCode.SUCCESS -> this
        ApplicationSqlErrorCode.VALIDATION_ERROR -> throw ApplicationValidationException(getSqlMessage())
        ApplicationSqlErrorCode.RESOURCE_NOT_FOUND -> throw ApplicationNotFoundException(getSqlMessage())
        ApplicationSqlErrorCode.STATE_CONFLICT -> throw ApplicationConflictException(getSqlMessage())
        ApplicationSqlErrorCode.UNEXPECTED_FAILURE -> throw ApplicationInternalException(getSqlMessage())
    }
}
