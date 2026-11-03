package com.oneview.banking.controller;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
    void shouldReturnNotFoundForMissingTransaction()
            throws Exception {

        when(transactionService.getTransactionById("UNKNOWN"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/UNKNOWN"))
                .andExpect(status().isNotFound());

        verify(transactionService)
                .getTransactionById("UNKNOWN");
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

    @Test
    void shouldReturnAllTransactionsWithData() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TX001");
        transaction.setCustomerId("C001");
        transaction.setMerchant("Uber");

        when(transactionService.getAllTransactions())
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId")
                        .value("TX001"))
                .andExpect(jsonPath("$[0].merchant")
                        .value("Uber"));

        verify(transactionService).getAllTransactions();
    }

    @Test
    void shouldReturnCustomerTransactionsWithData()
            throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TX002");
        transaction.setCustomerId("C001");
        transaction.setMerchant("Netflix");

        when(transactionService.getTransactionsByCustomer("C001"))
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions/customer/C001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId")
                        .value("C001"))
                .andExpect(jsonPath("$[0].merchant")
                        .value("Netflix"));

        verify(transactionService)
                .getTransactionsByCustomer("C001");
    }

    @Test
    void shouldReturnTransactionDetailsById()
            throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TX003");
        transaction.setMerchant("Pick n Pay");
        transaction.setCategory("Groceries");

        when(transactionService.getTransactionById("TX003"))
                .thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/TX003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId")
                        .value("TX003"))
                .andExpect(jsonPath("$.category")
                        .value("Groceries"));

        verify(transactionService)
                .getTransactionById("TX003");
    }

    @Test
    void shouldReturnCreatedTransactionDetails()
            throws Exception {

        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionId("TX004");
        savedTransaction.setMerchant("Spotify");

        when(transactionService.saveTransaction(any(Transaction.class)))
                .thenReturn(savedTransaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId")
                        .value("TX004"))
                .andExpect(jsonPath("$.merchant")
                        .value("Spotify"));

        verify(transactionService)
                .saveTransaction(any(Transaction.class));
    }
}
