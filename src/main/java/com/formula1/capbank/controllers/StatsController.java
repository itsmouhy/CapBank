package com.formula1.capbank.controllers;

import com.formula1.capbank.dtos.Transaction.DailyStatDTO;
import com.formula1.capbank.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statistiquesService;

    @GetMapping("/daily/compte/{compteId}")
    public ResponseEntity<List<DailyStatDTO>> getStatsParCompte(
            @PathVariable Long compteId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return ResponseEntity.ok(statistiquesService.getStatsJournalieresParCompte(compteId, start, end));
    }

    @GetMapping("/daily/test")
    public String test() {
        return "ok";
    }

}
