package com.anpatapain.coffwok.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.SizeLimitExceededException;
import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(
                "Password must be 6 for minimum and 40 for maximum"
        );
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<?> handleSizeExceededException(SizeLimitExceededException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), "max size is 500kb"
                )
        );
    }
}
