package com.oneview.banking.repository;

import com.oneview.banking.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void shouldSaveAndFindTransaction() {

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TEST001");
        transaction.setCustomerId("CUST001");
        transaction.setMerchant("Checkers");
        transaction.setAmount(new BigDecimal("250.00"));
        transaction.setCategory("Groceries");
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSource("MOCK_BANKING");

        repository.save(transaction);

        Transaction saved = repository
                .findByTransactionId("TEST001")
                .orElseThrow();

        assertEquals("CUST001", saved.getCustomerId());
        assertEquals("Checkers", saved.getMerchant());
        assertEquals(0,
                new BigDecimal("250.00").compareTo(saved.getAmount()));
    }
}
