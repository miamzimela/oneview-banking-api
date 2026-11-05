package com.oneview.banking.service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import com.oneview.banking.model.Transaction;
import com.oneview.banking.repository.TransactionRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class MockTransactionImportService {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public MockTransactionImportService(
            TransactionRepository transactionRepository,
            ObjectMapper objectMapper) {

        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void importTransactions() {

        importFromFile("mock-data/capitec-payments.json");
        importFromFile("mock-data/capitec-transactions.json");
        importFromFile("mock-data/capitec-transfers.json");
    }

    private void importFromFile(String filePath) {

        try {
            ClassPathResource resource =
                    new ClassPathResource(filePath);

            try (InputStream inputStream =
                         resource.getInputStream()) {

                List<Transaction> transactions =
                        objectMapper.readValue(
                                inputStream.readAllBytes(),
                                new TypeReference<>() {}                        );

                int importedCount = 0;

                for (Transaction transaction : transactions) {

                    boolean exists = transactionRepository
                            .findByTransactionId(
                                    transaction.getTransactionId()
                            )
                            .isPresent();

                    if (!exists) {

                        transaction.setId(null);

                        transactionRepository.save(transaction);

                        importedCount++;
                    }
                }

                System.out.println(
                        "Imported " + importedCount +
                                " transactions from " + filePath
                );
            }

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to import transactions from " + filePath,
                    e
            );
        }
    }
}
