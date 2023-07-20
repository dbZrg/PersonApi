package com.db.rbazadatak.config;

import com.db.rbazadatak.model.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Illegal Argument", exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Illegal Argument", exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Illegal Argument", exception));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handConstraintViolationException(ConstraintViolationException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Constraint Violation", exception));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handEntityNotFoundException(EntityNotFoundException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "Entity not found", exception));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        log.error(apiError.toString());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}