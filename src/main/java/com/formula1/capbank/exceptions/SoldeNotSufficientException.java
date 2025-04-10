package com.formula1.capbank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SoldeNotSufficientException extends Exception {
    public SoldeNotSufficientException(String message) {
        super(message);
    }
}
