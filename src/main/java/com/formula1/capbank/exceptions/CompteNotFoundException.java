package com.formula1.capbank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompteNotFoundException extends Exception {
    public CompteNotFoundException(String message) {
        super(message);
    }
}
