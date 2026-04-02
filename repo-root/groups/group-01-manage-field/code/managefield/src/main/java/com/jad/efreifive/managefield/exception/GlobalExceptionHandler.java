package com.jad.efreifive.managefield.exception;

import com.jad.efreifive.managefield.dto.ErrorResponseDto;
import com.jad.efreifive.managefield.dto.ValidationErrorDetailDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(ApiException exception) {
        return ResponseEntity.status(exception.getStatus())
            .body(ErrorResponseDto.builder()
                .error(exception.getError())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ValidationErrorDetailDto> details = exception.getBindingResult().getFieldErrors().stream()
            .map(this::toDetail)
            .toList();

        return ResponseEntity.badRequest()
            .body(ErrorResponseDto.builder()
                .error("ValidationError")
                .message("Request validation failed.")
                .details(details)
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
            .body(ErrorResponseDto.builder()
                .error("ValidationError")
                .message("Request body is malformed or unreadable.")
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDto.builder()
                .error("InternalServerError")
                .message("An unexpected error occurred.")
                .build());
    }

    private ValidationErrorDetailDto toDetail(FieldError fieldError) {
        return ValidationErrorDetailDto.builder()
            .field(fieldError.getField())
            .issue(fieldError.getCode())
            .message(fieldError.getDefaultMessage())
            .build();
    }
}
