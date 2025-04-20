package com.formula1.capbank.controllers;

import com.formula1.capbank.dtos.Compte.CompteHistoriqueDTO;
import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Compte.NouveauCompterequest;
import com.formula1.capbank.dtos.Transaction.CreditDTO;
import com.formula1.capbank.dtos.Transaction.DebitDTO;
import com.formula1.capbank.dtos.Transaction.TransferRequestDTO;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import com.formula1.capbank.services.ICompteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compte")
@AllArgsConstructor
public class CompteController {
    private ICompteService compteService;

    @PostMapping("/nouveau")
    public CompteResponse nouveauCompte(@RequestBody NouveauCompterequest compte){
        return compteService.nouveauCompte(compte);
    }

    @DeleteMapping("/supprimer/{id}")
    public void suppressionCompte(@PathVariable Long id, HttpServletResponse response){
        if(compteService.suppCompte(id)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            return;
        }
        response.setStatus(HttpServletResponse.SC_CONFLICT);
    }
    /**
     * Endpoint pour effectuer un débit sur un compte.
     *
     * @param debitDTO Les informations de débit.
     * @return La réponse HTTP avec le status et les données.
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */

    @PostMapping("/debit")
    public ResponseEntity<DebitDTO> debit(@RequestBody DebitDTO debitDTO) throws CompteNotFoundException, SoldeNotSufficientException {
        compteService.debit(debitDTO.numeroCompte(), debitDTO.montant(), debitDTO.description(), debitDTO.numeroCompteRecipient());
        return ResponseEntity.ok(debitDTO);
    }

    /**
     * Endpoint pour effectuer un crédit sur un compte.
     *
     * @param creditDto Les informations de crédit.
     * @return La réponse HTTP avec le status et les données.
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */

    @PostMapping("/credit")
    public ResponseEntity<CreditDTO> credit(@RequestBody CreditDTO creditDto) throws CompteNotFoundException {
        compteService.credit(creditDto.numeroCompte(), creditDto.montant(), creditDto.description(), creditDto.numeroCompteRecipient());
        return ResponseEntity.ok(creditDto);
    }

    /**
     *
     * @param transferRequestDTO Les informations du transfert.
     * @return La réponse HTTP avec le status de l'opération.
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws CompteNotFoundException, SoldeNotSufficientException {
        compteService.transfer(transferRequestDTO.numeroCompteSource(),
                transferRequestDTO.numeroCompteDest(),
                transferRequestDTO.montant());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Récupère l'historique paginé des transactions pour un compte donné.
     * @param compteId
     * @param page
     * @param size
     * @return
     * @throws CompteNotFoundException
     */

    @GetMapping("/{compteId}/pageTransactions")
    public CompteHistoriqueDTO getCompteHistorique(@PathVariable Long compteId,
                                                   @RequestParam (name="page", defaultValue = "0") int page,
                                                   @RequestParam(name="size", defaultValue = "3") int size) throws CompteNotFoundException {
        return compteService.getCompteHistorique(compteId, page, size);
    }


}
