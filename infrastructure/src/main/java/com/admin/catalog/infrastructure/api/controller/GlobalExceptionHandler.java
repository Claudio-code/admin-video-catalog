package com.admin.catalog.infrastructure.api.controller;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    ResponseEntity<ApiError> handlerNotFoundException(final NotFoundException notFoundException) {
        return ResponseEntity.status(NOT_FOUND)
            .body(ApiError.from(notFoundException));
    }

    @ExceptionHandler(value = DomainException.class)
    ResponseEntity<ApiError> handlerDomainException(final DomainException domainException) {
        return ResponseEntity.unprocessableEntity()
            .body(ApiError.from(domainException));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ValidationError> handlerValidationExceptionExceptions(final MethodArgumentNotValidException validException) {
        return ResponseEntity.status(BAD_REQUEST)
            .body(ValidationError.from(validException));
    }

    record ValidationError(List<Error> errors) {
        static ValidationError from(final MethodArgumentNotValidException validException) {
            final var fieldsErrors = validException.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .map(Error::new)
                .toList();
            return new ValidationError(fieldsErrors);
        }
    }

    record ApiError(String message, List<Error> errors) {
        static ApiError from(final DomainException domainException) {
            return new ApiError(domainException.getMessage(), domainException.getErrors());
        }
    }
}
