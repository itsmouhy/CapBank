package com.formula1.capbank.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface IDocumentService {
    ResponseEntity<byte[]> attestationRib(Long compteId, HttpServletRequest request,
                                          HttpServletResponse response);
    ResponseEntity<byte[]> releverBancaire(Long compteId, HttpServletRequest request,
                                           HttpServletResponse response);
}
