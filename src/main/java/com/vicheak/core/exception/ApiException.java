package com.vicheak.core.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends Exception {
    private final HttpStatus status;
    private final String message;
}
