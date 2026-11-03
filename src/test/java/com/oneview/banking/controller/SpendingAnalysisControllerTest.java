
package com.oneview.banking.controller;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.service.SpendingAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SpendingAnalysisControllerTest {

    private MockMvc mockMvc;
    private SpendingAnalysisService spendingAnalysisService;

    @BeforeEach
    void setUp() {
        spendingAnalysisService = mock(SpendingAnalysisService.class);

        SpendingAnalysisController controller =
                new SpendingAnalysisController(spendingAnalysisService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldGetCustomerTransactions() throws Exception {
        when(spendingAnalysisService.getCustomerTransactions("C001"))
                .thenReturn(List.of(new Transaction()));

        mockMvc.perform(get("/api/spending/C001/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(spendingAnalysisService)
                .getCustomerTransactions("C001");
    }

    @Test
    void shouldReturnEmptyTransactions() throws Exception {
        when(spendingAnalysisService.getCustomerTransactions("C001"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/spending/C001/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldGetTotalSpending() throws Exception {
        when(spendingAnalysisService.getTotalSpending("C001"))
                .thenReturn(new BigDecimal("2500.00"));

        mockMvc.perform(get("/api/spending/C001/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSpending").value(2500.00));

        verify(spendingAnalysisService)
                .getTotalSpending("C001");
    }

    @Test
    void shouldGetSpendingByCategory() throws Exception {
        when(spendingAnalysisService.getSpendingByCategory("C001"))
                .thenReturn(Map.of(
                        "Groceries", new BigDecimal("1200.00"),
                        "Transport", new BigDecimal("500.00")
                ));

        mockMvc.perform(get("/api/spending/C001/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Groceries").value(1200.00))
                .andExpect(jsonPath("$.Transport").value(500.00));

        verify(spendingAnalysisService)
                .getSpendingByCategory("C001");
    }

    @Test
    void shouldReturnEmptyCategories() throws Exception {
        when(spendingAnalysisService.getSpendingByCategory("C001"))
                .thenReturn(Map.of());

        mockMvc.perform(get("/api/spending/C001/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void shouldGetMonthlySpending() throws Exception {
        when(spendingAnalysisService.getMonthlySpending(
                "C001", 2026, 11))
                .thenReturn(new BigDecimal("1800.00"));

        mockMvc.perform(get("/api/spending/C001/monthly")
                        .param("year", "2026")
                        .param("month", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C001"))
                .andExpect(jsonPath("$.year").value(2026))
                .andExpect(jsonPath("$.month").value(11))
                .andExpect(jsonPath("$.totalSpending").value(1800.00));

        verify(spendingAnalysisService)
                .getMonthlySpending("C001", 2026, 11);
    }

    @Test
    void shouldRejectMissingMonth() throws Exception {
        mockMvc.perform(get("/api/spending/C001/monthly")
                        .param("year", "2026"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(spendingAnalysisService);
    }

    @Test
    void shouldRejectInvalidMonthFormat() throws Exception {
        mockMvc.perform(get("/api/spending/C001/monthly")
                        .param("year", "2026")
                        .param("month", "abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(spendingAnalysisService);
    }

    @Test
    void shouldGetHighestSpendingCategory() throws Exception {
        when(spendingAnalysisService
                .getHighestSpendingCategory("C001"))
                .thenReturn("Groceries");

        mockMvc.perform(get("/api/spending/C001/highest-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highestSpendingCategory")
                        .value("Groceries"));

        verify(spendingAnalysisService)
                .getHighestSpendingCategory("C001");
    }

    @Test
    void shouldHandleNoSpendingData() throws Exception {
        when(spendingAnalysisService
                .getHighestSpendingCategory("C001"))
                .thenReturn("No spending data");

        mockMvc.perform(get("/api/spending/C001/highest-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highestSpendingCategory")
                        .value("No spending data"));
    }
}
