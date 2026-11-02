package com.oneview.banking.repository;

import com.oneview.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(String customerId);

    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByCustomerIdAndCategory(
            String customerId,
            String category
    );
}
