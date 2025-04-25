package com.formula1.capbank.controllers;

import com.formula1.capbank.services.IDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/document")
@AllArgsConstructor
public class DocumentController {
    private IDocumentService documentService;

    @GetMapping("/{compteId}/rib")
    public ResponseEntity<byte[]> attestationRib(@PathVariable("compteId") Long id, HttpServletRequest request,
                                                 HttpServletResponse response){
        return documentService.attestationRib(id, request, response);
    }

    @GetMapping("/{compteId}/relever")
    public ResponseEntity<byte[]> releverBancaire(@PathVariable("compteId") Long id, HttpServletRequest request,
                                                 HttpServletResponse response){
        return documentService.releverBancaire(id,request,response);
    }

}
