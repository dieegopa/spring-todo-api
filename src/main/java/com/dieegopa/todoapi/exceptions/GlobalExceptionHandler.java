package com.dieegopa.todoapi.exceptions;


import com.dieegopa.todoapi.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(e.getMessage())
        );
    }

}
