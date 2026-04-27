package fr.efrei.managefield.service.exception

/**
 * Base exception type raised by the application layer independently of HTTP.
 */
sealed class ApplicationException(
    val errorType: ApplicationErrorType,
    override val message: String
) : RuntimeException(message)

/**
 * Application error categories translated to HTTP by the controller advice.
 */
enum class ApplicationErrorType(val error: String) {
    VALIDATION("ValidationError"),
    UNAUTHORIZED("Unauthorized"),
    NOT_FOUND("NotFound"),
    CONFLICT("Conflict"),
    INTERNAL_ERROR("InternalError")
}

class ApplicationValidationException(message: String) :
    ApplicationException(ApplicationErrorType.VALIDATION, message)

class ApplicationUnauthorizedException(message: String) :
    ApplicationException(ApplicationErrorType.UNAUTHORIZED, message)

class ApplicationNotFoundException(message: String) :
    ApplicationException(ApplicationErrorType.NOT_FOUND, message)

class ApplicationConflictException(message: String) :
    ApplicationException(ApplicationErrorType.CONFLICT, message)

class ApplicationInternalException(message: String) :
    ApplicationException(ApplicationErrorType.INTERNAL_ERROR, message)
