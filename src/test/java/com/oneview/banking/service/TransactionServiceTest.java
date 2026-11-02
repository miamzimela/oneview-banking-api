package com.oneview.banking.service;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void shouldGetAllTransactions() {
        Transaction transaction = new Transaction();

        when(transactionRepository.findAll())
                .thenReturn(List.of(transaction));

        List<Transaction> result =
                transactionService.getAllTransactions();

        assertEquals(1, result.size());
        verify(transactionRepository).findAll();
    }

    @Test
    void shouldGetTransactionsByCustomer() {
        Transaction transaction = new Transaction();

        when(transactionRepository.findByCustomerId("C001"))
                .thenReturn(List.of(transaction));

        List<Transaction> result =
                transactionService.getTransactionsByCustomer("C001");

        assertEquals(1, result.size());
        verify(transactionRepository).findByCustomerId("C001");
    }

    @Test
    void shouldFindTransactionById() {
        Transaction transaction = new Transaction();

        when(transactionRepository.findByTransactionId("TX001"))
                .thenReturn(Optional.of(transaction));

        Optional<Transaction> result =
                transactionService.getTransactionById("TX001");

        assertTrue(result.isPresent());
        verify(transactionRepository).findByTransactionId("TX001");
    }

    @Test
    void shouldSaveTransaction() {
        Transaction transaction = new Transaction();

        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        Transaction saved =
                transactionService.saveTransaction(transaction);

        assertSame(transaction, saved);
        verify(transactionRepository).save(transaction);
    }
}