package fr.efrei.managefield.controller

import fr.efrei.managefield.controller.dto.response.ErrorResponseDto
import fr.efrei.managefield.controller.dto.response.ValidationErrorDetailResponseDto
import fr.efrei.managefield.service.exception.ApplicationErrorType
import fr.efrei.managefield.service.exception.ApplicationException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Translates application exceptions into HTTP responses.
 */
@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(exception: ApplicationException): ResponseEntity<ErrorResponseDto> {
        val status = when (exception.errorType) {
            ApplicationErrorType.VALIDATION -> HttpStatus.BAD_REQUEST
            ApplicationErrorType.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
            ApplicationErrorType.NOT_FOUND -> HttpStatus.NOT_FOUND
            ApplicationErrorType.CONFLICT -> HttpStatus.CONFLICT
            ApplicationErrorType.INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity
            .status(status)
            .body(
                ErrorResponseDto(
                    error = exception.errorType.error,
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        val details = exception.bindingResult.fieldErrors.map {
            ValidationErrorDetailResponseDto(
                field = it.field,
                issue = it.code ?: "Invalid",
                message = it.defaultMessage ?: "invalid value"
            )
        }

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponseDto(
                    error = ApplicationErrorType.VALIDATION.error,
                    message = "invalid request",
                    details = details
                )
            )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(exception: ConstraintViolationException): ResponseEntity<ErrorResponseDto> {
        val details = exception.constraintViolations.map {
            ValidationErrorDetailResponseDto(
                field = it.propertyPath.toString(),
                issue = it.constraintDescriptor.annotation.annotationClass.simpleName ?: "Invalid",
                message = it.message
            )
        }

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponseDto(
                    error = ApplicationErrorType.VALIDATION.error,
                    message = "invalid request",
                    details = details
                )
            )
    }
}
