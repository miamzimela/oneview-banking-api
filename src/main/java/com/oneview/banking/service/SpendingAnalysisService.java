package com.oneview.banking.service;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SpendingAnalysisService {

    private final TransactionRepository transactionRepository;

    public SpendingAnalysisService(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get all transactions belonging to a customer
    public List<Transaction> getCustomerTransactions(
            String customerId) {

        return transactionRepository
                .findByCustomerId(customerId);
    }

    // Calculate total spending for a customer
    public BigDecimal getTotalSpending(String customerId) {

        return calculateTotal(
                getCustomerTransactions(customerId));
    }

    // Calculate spending by category
    public Map<String, BigDecimal> getSpendingByCategory(
            String customerId) {

        return getCustomerTransactions(customerId)
                .stream()
                .filter(t -> t.getAmount() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() == null
                                || t.getCategory().isBlank()
                                ? "Other"
                                : t.getCategory(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    // Calculate spending for a specific month
    public BigDecimal getMonthlySpending(
            String customerId, int year, int month) {

        YearMonth selectedMonth = YearMonth.of(year, month);

        List<Transaction> monthlyTransactions =
                getCustomerTransactions(customerId)
                        .stream()
                        .filter(t -> t.getTransactionDate() != null)
                        .filter(t -> YearMonth.from(
                                t.getTransactionDate()
                        ).equals(selectedMonth))
                        .toList();

        return calculateTotal(monthlyTransactions);
    }

    // Find the customer's highest spending category
    public String getHighestSpendingCategory(
            String customerId) {

        return getSpendingByCategory(customerId)
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No spending data");
    }

    // Calculate total for a list of transactions
    private BigDecimal calculateTotal(
            List<Transaction> transactions) {

        return transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
