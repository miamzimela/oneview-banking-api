package com.oneview.banking.controller;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

class TransactionControllerTest {

    private MockMvc mockMvc;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);

        TransactionController controller =
                new TransactionController(transactionService);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void shouldGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(transactionService).getAllTransactions();
    }

    @Test
    void shouldGetTransactionsByCustomer() throws Exception {
        when(transactionService.getTransactionsByCustomer("C001"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/customer/C001"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(transactionService)
                .getTransactionsByCustomer("C001");
    }

    @Test
    void shouldFindTransactionById() throws Exception {
        Transaction transaction = new Transaction();

        when(transactionService.getTransactionById("TX001"))
                .thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/TX001"))
                .andExpect(status().isOk());

        verify(transactionService)
                .getTransactionById("TX001");
    }

    @Test
    void shouldReturnNotFoundForMissingTransaction() throws Exception {
        when(transactionService.getTransactionById("UNKNOWN"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();

        when(transactionService.saveTransaction(any(Transaction.class)))
                .thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated());

        verify(transactionService)
                .saveTransaction(any(Transaction.class));
    }
}