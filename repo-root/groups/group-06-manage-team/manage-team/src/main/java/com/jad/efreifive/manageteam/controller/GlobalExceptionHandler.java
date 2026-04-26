package com.jad.efreifive.manageteam.controller;

import com.jad.efreifive.manageteam.service.ServiceOperationException;
import com.jad.efreifive.manageteam.service.TeamServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceOperationException.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(ServiceOperationException exception) {
        GlobalExceptionHandler.log.error("Service operation error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(this.errorBody(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    private Map<String, Object> errorBody(String message, int status) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "message", message
                     );
    }

    @ExceptionHandler(TeamServiceException.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(TeamServiceException exception) {
        GlobalExceptionHandler.log.error("Team service error: {}", exception.getMessage());
        final HttpStatus httpStatus = DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(
                exception.getDomainErrorCode());
        return ResponseEntity.status(httpStatus)
                .body(this.errorBody(exception.getMessage(), httpStatus.value()));
    }
}


