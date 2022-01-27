package com.example.urlshortener.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class UrlValidationException extends ResponseStatusException {

    public UrlValidationException(HttpStatus status, String message) {
        super(status, message);
        ErrorAttributeOptions.of(MESSAGE);
    }
}