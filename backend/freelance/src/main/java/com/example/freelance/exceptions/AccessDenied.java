package com.example.freelance.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccessDenied extends RuntimeException {
    public AccessDenied(String message) {
        super(message);
    }
}
