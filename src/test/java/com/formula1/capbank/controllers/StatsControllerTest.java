package com.formula1.capbank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formula1.capbank.dtos.Transaction.DailyStatDTO;
import com.formula1.capbank.services.StatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    void testGetStatsParCompte() throws Exception {
        List<DailyStatDTO> stats = Arrays.asList(
                new DailyStatDTO(LocalDate.of(2025, 4, 8), 100.0, 50.0),
                new DailyStatDTO(LocalDate.of(2025, 4, 9), 200.0, 30.0)
        );

        when(statsService.getStatsJournalieresParCompte(1L, LocalDate.of(2025, 4, 8), LocalDate.of(2025, 4, 9)))
                .thenReturn(stats);

        mockMvc.perform(get("/api/stats/daily/compte/1")
                        .param("start", "2025-04-08")
                        .param("end", "2025-04-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-04-08"))
                .andExpect(jsonPath("$[0].totalDepot").value(100.0))
                .andExpect(jsonPath("$[0].totalRetrait").value(50.0))
                .andExpect(jsonPath("$[1].date").value("2025-04-09"))
                .andExpect(jsonPath("$[1].totalDepot").value(200.0))
                .andExpect(jsonPath("$[1].totalRetrait").value(30.0));
    }

    @Test
    void testTestEndpoint() throws Exception {
        mockMvc.perform(get("/api/stats/daily/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
