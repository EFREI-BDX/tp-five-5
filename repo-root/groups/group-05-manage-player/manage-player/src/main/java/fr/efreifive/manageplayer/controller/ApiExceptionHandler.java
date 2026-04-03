package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.dto.ErrorDetail;
import fr.efreifive.manageplayer.dto.ErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<ErrorDetail> details = exception.getBindingResult().getFieldErrors().stream()
            .map(this::toErrorDetail)
            .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "Les donnees fournies sont invalides.", details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "Les donnees fournies sont invalides.", List.of()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        String code = switch (status) {
            case NOT_FOUND -> "PLAYER_NOT_FOUND";
            case CONFLICT -> "PLAYER_ALREADY_EXISTS";
            case UNAUTHORIZED -> "UNAUTHORIZED";
            case BAD_REQUEST -> "VALIDATION_ERROR";
            default -> "INTERNAL_SERVER_ERROR";
        };

        return ResponseEntity.status(status)
            .body(new ErrorResponse(code, exception.getReason() != null ? exception.getReason() : status.getReasonPhrase(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "Une erreur inattendue s'est produite.", List.of()));
    }

    private ErrorDetail toErrorDetail(FieldError fieldError) {
        return new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
