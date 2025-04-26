package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Compte.CompteHistoriqueDTO;
import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Compte.NouveauCompterequest;
import com.formula1.capbank.dtos.Transaction.TransactionDTO;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.entities.Transactions;
import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.enums.TransactionStatus;
import com.formula1.capbank.enums.TransactionType;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import com.formula1.capbank.mappers.CompteMapper;
import com.formula1.capbank.mappers.TransactionMapper;
import com.formula1.capbank.repositories.CompteRepository;
import com.formula1.capbank.repositories.TransactionsRepository;
import com.formula1.capbank.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CompteService implements ICompteService{
    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;
    private final UtilisateurRepository utilisateurRepo;
    private final TransactionsRepository transactionsRepository;

    public CompteService(CompteRepository compteRepository,
                         CompteMapper compteMapper,
                         UtilisateurRepository utilisateurRepo,
                         TransactionsRepository transactionsRepository,
                         TransactionMapper transactionMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
        this.utilisateurRepo = utilisateurRepo;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public CompteResponse detailsCompte(Long id) {
        if(id == null || id.equals(0L))
            throw new IllegalArgumentException("Identifiant n'est pas renseigner");
        Compte compte = compteRepository.findById(id).orElse(null);
        if(compte == null)
            throw new IllegalArgumentException("Le compte n'existe pas");

        return compteMapper.toResponse(compte);
    }

    @Override
    public CompteResponse nouveauCompte(NouveauCompterequest compte) {
        // Valider l'identifiant recu
        if(compte.idUtilisateur() == null)
            throw new IllegalArgumentException("Veuillez validez les informations renseigner");
        Utilisateur user = utilisateurRepo.findById(compte.idUtilisateur()).orElse(null);

        // Valider l'existence d'utilisateur a qui ont ajoutera le compte
        if(user == null)
            throw new IllegalArgumentException("L'utilisateur n'existe pas");

        // création du compte
        Long rib = generationRib();
        String numeroCompte = generationNoCompte();
        Compte compteCreate = Compte.builder()
                .date(LocalDate.now())
                .numeroCompte(numeroCompte)
                .solde(compte.solde())
                .rib(rib)
                .utilisateur(user).build();
        compteCreate = compteRepository.save(compteCreate);
        return compteMapper.toResponse(compteCreate);

    }

    // methode de generation de rib a partir du date pour qu'il soit unique
    private Long generationRib(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return 4779000000000000L + Long.parseLong(now.format(formatter));
    }

    // methode de generation de numero de compte
    private String generationNoCompte() {
        String numero;
        do {
            numero = "CC" + (int)(Math.random() * 1_000_000_000);
        } while (compteRepository.existsByNumeroCompte(numero));
        return numero;
    }

    @Override
    public boolean suppCompte(Long id) {
        Compte compte = compteRepository.findById(id).orElse(null);

        if(compte == null)
            throw new IllegalArgumentException("Le compte n'existe pas");
        if(compte.getUtilisateur().getComptes().size() <= 1)
            throw new IllegalArgumentException("Impossible de supprimer le compte. Nombre de comptes ne peut pas etre inferieur a 1");
        compteRepository.delete(compte);
        return true;
    }

    @Override
    public void debit(String numeroCompte, Double montant, String description, String recipientNum) throws CompteNotFoundException, SoldeNotSufficientException {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new CompteNotFoundException("Compte non trouvé"));

        Transactions transactions = Transactions.builder()
                .compte(compte)
                .type(TransactionType.RETRAIT)
                .montant(montant)
                .date(LocalDate.now())
                .description(description)
                .noCompteRecipient(recipientNum)
                .status(TransactionStatus.PENDING)
                .build();
        transactionsRepository.save(transactions);

        try {
            if (compte.getSolde() < montant)
                throw new SoldeNotSufficientException("Solde non suffisant !!!");
            compte.setSolde(compte.getSolde() - montant);
            compteRepository.save(compte);

            transactions.setStatus(TransactionStatus.COMPLETED);
            transactionsRepository.save(transactions);
        } catch (SoldeNotSufficientException e) {
            transactions.setStatus(TransactionStatus.CANCELED);
            transactionsRepository.save(transactions);
            throw e;
        }

        log.info("Débit de {} effectué sur le compte {}", montant, recipientNum);
    }

    @Override
    public void credit(String numeroCompte, Double montant, String description, String recipientNum) throws CompteNotFoundException {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new CompteNotFoundException("Compte non trouvé"));

        Transactions transactions = Transactions.builder()
                .compte(compte)
                .type(TransactionType.DEPOT)
                .montant(montant)
                .date(LocalDate.now())
                .description(description)
                .status(TransactionStatus.COMPLETED)
                .noCompteRecipient(recipientNum)
                .build();
        transactionsRepository.save(transactions);

        try {
            compte.setSolde(compte.getSolde() + montant);
            compteRepository.save(compte);

            transactions.setStatus(TransactionStatus.COMPLETED);
            transactionsRepository.save(transactions);
        } catch (Exception e) {
            transactions.setStatus(TransactionStatus.CANCELED);
            transactionsRepository.save(transactions);
            throw e;
        }
        log.info("Crédit de {} effectué sur le compte {}", montant, recipientNum);
    }

    @Override
    public void transfer(String numeroCompteSource, String numeroCompteDest, Double montant) throws CompteNotFoundException, SoldeNotSufficientException {
        debit(numeroCompteSource, montant, "Transférer à " + numeroCompteDest, numeroCompteDest);
        credit(numeroCompteDest, montant, "Transférer depuis " + numeroCompteSource, numeroCompteSource);
        log.info("Transfet de {} de compte {} vers compte {}", montant, numeroCompteSource, numeroCompteDest);
    }

    @Override
    public CompteHistoriqueDTO getCompteHistorique(Long compteId, int page, int size) throws CompteNotFoundException {
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new CompteNotFoundException("Compte non trouvé !!!"));
        Page<Transactions> transactions = transactionsRepository.findByCompteIdOrderByDateDesc(compteId, PageRequest.of(page, size));
        List<TransactionDTO> transactionDTOS = transactions.getContent()
                .stream()
                .map(tr -> new TransactionDTO(
                        tr.getId(),
                        tr.getMontant(),
                        tr.getType(),
                        tr.getDescription(),
                        tr.getDate(),
                        tr.getStatus(),
                        tr.getNoCompteRecipient()
                ))
                .collect(Collectors.toList());

        return new CompteHistoriqueDTO(
                compte.getId(),
                compte.getSolde(),
                page,
                transactions.getTotalPages(),
                size,
                transactionDTOS
        );
    }

}
