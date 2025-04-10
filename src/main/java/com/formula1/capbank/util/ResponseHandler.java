package com.formula1.capbank.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message,
                                                          HttpStatus status,
                                                          Object body) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("body", body);
        return new ResponseEntity<>(map, status);
    }
}
