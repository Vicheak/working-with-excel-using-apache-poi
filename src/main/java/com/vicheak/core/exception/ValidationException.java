package com.vicheak.core.exception;

import com.vicheak.core.base.BaseError;
import com.vicheak.core.base.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = new ArrayList<>() {{
            ex.getFieldErrors().forEach(fieldError -> add(FieldError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build()));
        }};

        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(9999)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseError);
    }

}
