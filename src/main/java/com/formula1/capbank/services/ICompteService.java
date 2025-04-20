package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Compte.CompteHistoriqueDTO;
import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Compte.NouveauCompterequest;
import com.formula1.capbank.entities.Compte;
import com.formula1.capbank.exceptions.CompteNotFoundException;
import com.formula1.capbank.exceptions.SoldeNotSufficientException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public interface ICompteService {
    CompteResponse detailsCompte(Long id);
    CompteResponse nouveauCompte(NouveauCompterequest compte);
    boolean suppCompte(Long id);

    /**
     * Effectue un débit sur le compte spécifié.
     *
     * @param numeroCompte
     * @param montant
     * @param description
     * @param recipientNum
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */
    void debit(String numeroCompte, Double montant, String description, String recipientNum) throws CompteNotFoundException, SoldeNotSufficientException;

    /**
     * Effectue un crédit sur le compte spécifié
     *
     * @param numeroCompte
     * @param montant
     * @param description
     * @param recipientNum
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */
    void credit(String numeroCompte, Double montant, String description, String recipientNum) throws CompteNotFoundException;

    /**
     * Effectue un transfert sur le compte spécifié
     *
     * @param numeroCompteSource
     * @param numeroCompteDest
     * @param montant
     * @throws CompteNotFoundException
     * @throws SoldeNotSufficientException
     */
    void transfer(String numeroCompteSource, String numeroCompteDest, Double montant) throws CompteNotFoundException, SoldeNotSufficientException;

    /**
     * Récupère l'historique des transactions pour un compte donné, avec pagination.
     * @param compteId
     * @param page Le numéro de la page à récupérer (commence à 0).
     * @param size Le nombre d'élements par page.
     * @return Un objet {@link CompteHistoriqueDTO} contenant les détails de l'historique des transactions du compte spécifié.
     * @throws CompteNotFoundException
     */
    CompteHistoriqueDTO getCompteHistorique(Long compteId, int page, int size) throws CompteNotFoundException;

}
