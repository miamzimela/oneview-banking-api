package com.oneview.banking.service;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpendingAnalysisServiceTest {

    private TransactionRepository transactionRepository;
    private SpendingAnalysisService spendingAnalysisService;

    private static final String CUSTOMER_ID = "CUSTOMER001";

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);

        spendingAnalysisService =
                new SpendingAnalysisService(transactionRepository);
    }

    private Transaction createTransaction(
            String merchant,
            String category,
            String amount,
            LocalDate date) {

        Transaction transaction = new Transaction();

        transaction.setCustomerId(CUSTOMER_ID);
        transaction.setMerchant(merchant);
        transaction.setCategory(category);
        transaction.setAmount(
                amount == null ? null : new BigDecimal(amount));
        transaction.setTransactionDate(date);

        return transaction;
    }

    @Test
    void shouldGetCustomerTransactions() {

        Transaction transaction = createTransaction(
                "Pick 'n Pay",
                "Groceries",
                "500.00",
                LocalDate.of(2026, 11, 1)
        );

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(transaction));

        List<Transaction> result =
                spendingAnalysisService
                        .getCustomerTransactions(CUSTOMER_ID);

        assertEquals(1, result.size());
        assertEquals("Pick 'n Pay", result.get(0).getMerchant());

        verify(transactionRepository)
                .findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void shouldCalculateTotalSpending() {

        Transaction groceries = createTransaction(
                "Checkers", "Groceries", "850.00",
                LocalDate.of(2026, 11, 1));

        Transaction transport = createTransaction(
                "Uber", "Transport", "200.00",
                LocalDate.of(2026, 11, 2));

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(groceries, transport));

        BigDecimal result =
                spendingAnalysisService.getTotalSpending(CUSTOMER_ID);

        assertEquals(0,
                new BigDecimal("1050.00").compareTo(result));
    }

    @Test
    void shouldCalculateSpendingByCategory() {

        Transaction first = createTransaction(
                "Pick 'n Pay", "Groceries", "500.00",
                LocalDate.of(2026, 11, 1));

        Transaction second = createTransaction(
                "Checkers", "Groceries", "300.00",
                LocalDate.of(2026, 11, 2));

        Transaction third = createTransaction(
                "Uber", "Transport", "150.00",
                LocalDate.of(2026, 11, 3));

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(first, second, third));

        Map<String, BigDecimal> result =
                spendingAnalysisService
                        .getSpendingByCategory(CUSTOMER_ID);

        assertEquals(0,
                new BigDecimal("800.00")
                        .compareTo(result.get("Groceries")));

        assertEquals(0,
                new BigDecimal("150.00")
                        .compareTo(result.get("Transport")));

        assertEquals(2, result.size());
    }

    @Test
    void shouldCalculateMonthlySpending() {

        Transaction novemberFirst = createTransaction(
                "Checkers", "Groceries", "600.00",
                LocalDate.of(2026, 11, 1));

        Transaction novemberSecond = createTransaction(
                "Uber Eats", "Food & Dining", "250.00",
                LocalDate.of(2026, 11, 3));

        Transaction october = createTransaction(
                "Netflix", "Entertainment", "150.00",
                LocalDate.of(2026, 10, 20));

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(
                        novemberFirst, novemberSecond, october));

        BigDecimal result =
                spendingAnalysisService
                        .getMonthlySpending(CUSTOMER_ID, 2026, 11);

        assertEquals(0,
                new BigDecimal("850.00").compareTo(result));
    }

    @Test
    void shouldIdentifyHighestSpendingCategory() {

        Transaction groceries = createTransaction(
                "Pick 'n Pay", "Groceries", "1200.00",
                LocalDate.of(2026, 11, 1));

        Transaction entertainment = createTransaction(
                "Amazon Prime Video", "Entertainment", "100.00",
                LocalDate.of(2026, 11, 2));

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(groceries, entertainment));

        String result =
                spendingAnalysisService
                        .getHighestSpendingCategory(CUSTOMER_ID);

        assertEquals("Groceries", result);
    }

    @Test
    void shouldHandleCustomerWithNoTransactions() {

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of());

        assertEquals(0,
                BigDecimal.ZERO.compareTo(
                        spendingAnalysisService
                                .getTotalSpending(CUSTOMER_ID)));

        assertTrue(spendingAnalysisService
                .getSpendingByCategory(CUSTOMER_ID).isEmpty());

        assertEquals("No spending data",
                spendingAnalysisService
                        .getHighestSpendingCategory(CUSTOMER_ID));
    }

    @Test
    void shouldHandleMissingAmountAndCategory() {

        Transaction missingAmount = createTransaction(
                "Unknown Merchant", "Shopping", null,
                LocalDate.of(2026, 11, 1));

        Transaction missingCategory = createTransaction(
                "Unknown Merchant", null, "75.00",
                LocalDate.of(2026, 11, 2));

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(
                        missingAmount, missingCategory));

        Map<String, BigDecimal> result =
                spendingAnalysisService
                        .getSpendingByCategory(CUSTOMER_ID);

        assertEquals(0,
                new BigDecimal("75.00")
                        .compareTo(result.get("Other")));

        assertFalse(result.containsKey("Shopping"));
    }

    @Test
    void shouldIgnoreTransactionsWithoutDatesInMonthlySpending() {

        Transaction dated = createTransaction(
                "Checkers", "Groceries", "200.00",
                LocalDate.of(2026, 11, 1));

        Transaction undated = createTransaction(
                "Uber", "Transport", "100.00", null);

        when(transactionRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(dated, undated));

        BigDecimal result =
                spendingAnalysisService
                        .getMonthlySpending(CUSTOMER_ID, 2026, 11);

        assertEquals(0,
                new BigDecimal("200.00").compareTo(result));
    }
}
