package com.formula1.capbank.mappers;

import com.formula1.capbank.dtos.Compte.CompteResponse;
import com.formula1.capbank.dtos.Compte.RibResponse;
import com.formula1.capbank.entities.Compte;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CompteMapper {
    public CompteResponse toResponse(Compte compte){
        return new CompteResponse(compte.getId(), compte.getSolde(), compte.getDate(), compte.getRib());
    }
    public RibResponse toRibResponse(Compte compte){
        return new RibResponse(compte.getUtilisateur().getNom(),
                compte.getUtilisateur().getPrenom(), compte.getRib(), LocalDate.now());
    }
}
