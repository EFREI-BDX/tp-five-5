package org.efrei.five.apimanagematch.application.exception;

import org.efrei.five.apimanagematch.domain.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handle(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
