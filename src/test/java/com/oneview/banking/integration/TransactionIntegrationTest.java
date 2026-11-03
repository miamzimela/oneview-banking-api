package com.oneview.banking.integration;

import tools.jackson.databind.ObjectMapper;
import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    private Transaction createTransaction(
            String transactionId,
            String customerId,
            String merchant,
            BigDecimal amount) {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(transactionId);
        transaction.setCustomerId(customerId);
        transaction.setMerchant(merchant);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.of(2026, 11, 3));
        transaction.setSource("Capitec");

        return transaction;
    }

    @Test
    void shouldCreateAndAutomaticallyCategoriseTransaction()
            throws Exception {

        Transaction transaction = createTransaction(
                "TX1001",
                "C001",
                "Uber",
                new BigDecimal("150.00")
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TX1001"))
                .andExpect(jsonPath("$.merchant").value("Uber"))
                .andExpect(jsonPath("$.category").value("Transport"));

        Transaction saved = transactionRepository
                .findByTransactionId("TX1001")
                .orElseThrow();

        assertEquals("Transport", saved.getCategory());
        assertEquals("C001", saved.getCustomerId());
    }

    @Test
    void shouldRetrieveSavedTransactionById()
            throws Exception {

        Transaction transaction = createTransaction(
                "TX1002",
                "C002",
                "Netflix",
                new BigDecimal("199.00")
        );

        transaction.setCategory("Entertainment");
        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/transactions/TX1002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TX1002"))
                .andExpect(jsonPath("$.category").value("Entertainment"));
    }

    @Test
    void shouldRetrieveTransactionsByCustomer()
            throws Exception {

        Transaction first = createTransaction(
                "TX1003", "C003", "Uber",
                new BigDecimal("100.00")
        );
        first.setCategory("Transport");

        Transaction second = createTransaction(
                "TX1004", "C003", "Netflix",
                new BigDecimal("150.00")
        );
        second.setCategory("Entertainment");

        transactionRepository.save(first);
        transactionRepository.save(second);

        mockMvc.perform(get("/api/transactions/customer/C003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnNotFoundForMissingTransaction()
            throws Exception {

        mockMvc.perform(get("/api/transactions/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRetrieveAllSavedTransactions()
            throws Exception {

        Transaction transaction = createTransaction(
                "TX1005",
                "C004",
                "Spotify",
                new BigDecimal("79.99")
        );

        transaction.setCategory("Entertainment");
        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value("TX1005"));
    }
}
