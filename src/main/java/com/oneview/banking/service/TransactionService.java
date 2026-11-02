package com.oneview.banking.service;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCustomer(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    public Optional<Transaction> getTransactionById(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}

