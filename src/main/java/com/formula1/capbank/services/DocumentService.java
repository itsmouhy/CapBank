package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Compte.RibResponse;
import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.mappers.CompteMapper;
import com.formula1.capbank.mappers.TransactionMapper;
import com.formula1.capbank.repositories.CompteRepository;
import com.formula1.capbank.repositories.TransactionsRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.TemporalAdjusters.*;

@Service
@AllArgsConstructor
public class DocumentService implements IDocumentService{
    private SpringTemplateEngine templateEngine;
    private CompteRepository compteRepo;
    private CompteMapper compteMapper;
    private TransactionsRepository transactionsRepository;
    private TransactionMapper transactionMapper;

    public static WebContext createContext(HttpServletRequest request, HttpServletResponse response){
        var application = JakartaServletWebApplication.buildApplication(request.getServletContext());
        var exchange = application.buildExchange(request, response);
        return new WebContext(exchange);
    }
    @Override
    public ResponseEntity<byte[]> attestationRib(Long compteId, HttpServletRequest request, HttpServletResponse response) {
        // Verify if the account exists
        Compte compte = compteRepo.findById(compteId).orElse(null);
        if(compte == null)
            throw new IllegalArgumentException("Le compte souhaité n'existe pas");
        // Map the account to the record
        RibResponse compteRib = compteMapper.toRibResponse(compte);
        // Prepare Thymeleaf context with variables
        WebContext context = createContext(request, response);

        context.setVariable("ribResponse", compteRib);


        // Process Thymeleaf template into HTML

        String htmlContent = templateEngine.process("ribHtmlTemplate", context);


        // Convert HTML to PDF

        byte[] pdfBytes;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setBaseUri("http://localhost:8080"); // needed for images/CSS


            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

            pdfBytes = outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException("Failed to generate PDF", e);
        }
        // Build filename
        String fileName = String.join("_",
                compteRib.nom(),
                compteRib.prenom())+ ".pdf";
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        // Return PDF as HTTP response
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    private List<TransactionDTO> transactionsMoisPreced(Long compteId){
        List<Transactions> transactions = transactionsRepository.findByCompteIdOrderByDateDesc(compteId).orElse(null);
        if(transactions == null)
            throw new IllegalArgumentException("Aucune transaction existe");
        List<TransactionDTO> results = new ArrayList<>();
        transactions = transactions.stream().filter(transac -> transac.getDate().getMonth()
                .equals(LocalDate.now().getMonth().minus(1L))).toList();
        transactions.forEach(tr -> results.add(transactionMapper.fromTransaction(tr)));
        return results;
    }
    @Override
    public ResponseEntity<byte[]> releverBancaire(Long compteId, HttpServletRequest request, HttpServletResponse response) {
        // Verify if the account exists
        Compte compte = compteRepo.findById(compteId).orElse(null);
        if(compte == null)
            throw new IllegalArgumentException("Le compte souhaité n'existe pas");

        // Map the account to the record
        RibResponse compteRib = compteMapper.toRibResponse(compte);

        // Prepare Thymeleaf context with variables
        WebContext context = createContext(request, response);

        context.setVariable("transactions", transactionsMoisPreced(compteId));
        context.setVariable("compte", compteRib);
        context.setVariable("dateDebut", LocalDate.now().minusMonths(1).withDayOfMonth(1));
        context.setVariable("dateFin", LocalDate.now().minusMonths(1)
                .withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())));


        // Process Thymeleaf template into HTML

        String htmlContent = templateEngine.process("transactionsHtmlTemplate", context);


        // Convert HTML to PDF

        byte[] pdfBytes;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setBaseUri("http://localhost:8080"); // needed for images/CSS


            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

            pdfBytes = outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException("Failed to generate PDF", e);
        }
        // Build filename
        String fileName = String.join("_",
                compte.getUtilisateur().getNom(),
                compte.getUtilisateur().getPrenom())+ ".pdf";
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        // Return PDF as HTTP response
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
