package com.oneview.banking.controller;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.service.SpendingAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spending")
public class SpendingAnalysisController {

    private final SpendingAnalysisService spendingAnalysisService;

    public SpendingAnalysisController(
            SpendingAnalysisService spendingAnalysisService) {
        this.spendingAnalysisService = spendingAnalysisService;
    }

    // Retrieve all transactions for a customer
    @GetMapping("/{customerId}/transactions")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                spendingAnalysisService.getCustomerTransactions(customerId)
        );
    }

    // Calculate total spending for a customer
    @GetMapping("/{customerId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalSpending(
            @PathVariable String customerId) {

        BigDecimal total =
                spendingAnalysisService.getTotalSpending(customerId);

        return ResponseEntity.ok(Map.of("totalSpending", total));
    }

    // Retrieve spending grouped by category
    @GetMapping("/{customerId}/categories")
    public ResponseEntity<Map<String, BigDecimal>> getSpendingByCategory(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                spendingAnalysisService.getSpendingByCategory(customerId)
        );
    }

    // Retrieve spending for a selected month
    @GetMapping("/{customerId}/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlySpending(
            @PathVariable String customerId,
            @RequestParam int year,
            @RequestParam int month) {

        BigDecimal total = spendingAnalysisService.getMonthlySpending(
                customerId, year, month
        );

        return ResponseEntity.ok(Map.of(
                "customerId", customerId,
                "year", year,
                "month", month,
                "totalSpending", total
        ));
    }

    // Retrieve the highest spending category
    @GetMapping("/{customerId}/highest-category")
    public ResponseEntity<Map<String, String>> getHighestSpendingCategory(
            @PathVariable String customerId) {

        String category =
                spendingAnalysisService.getHighestSpendingCategory(customerId);

        return ResponseEntity.ok(
                Map.of("highestSpendingCategory", category)
        );
    }
}
