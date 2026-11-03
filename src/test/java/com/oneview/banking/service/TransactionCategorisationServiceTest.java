package com.oneview.banking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionCategorisationServiceTest {

    private TransactionCategorisationService categorisationService;

    @BeforeEach
    void setUp() {
        categorisationService = new TransactionCategorisationService();
    }

    @Test
    void shouldCategoriseGroceries() {
        assertEquals("Groceries",
                categorisationService.categoriseTransaction("Pick 'n Pay"));

        assertEquals("Groceries",
                categorisationService.categoriseTransaction("Checkers"));

        assertEquals("Groceries",
                categorisationService.categoriseTransaction("Shoprite"));
    }

    @Test
    void shouldCategoriseTransport() {
        assertEquals("Transport",
                categorisationService.categoriseTransaction("Uber"));

        assertEquals("Transport",
                categorisationService.categoriseTransaction("Bolt"));

        assertEquals("Transport",
                categorisationService.categoriseTransaction("Gautrain"));
    }

    @Test
    void shouldCategoriseUberEatsAsFoodAndDining() {
        assertEquals("Food & Dining",
                categorisationService.categoriseTransaction("Uber Eats"));
    }

    @Test
    void shouldCategoriseEntertainment() {
        assertEquals("Entertainment",
                categorisationService.categoriseTransaction("Netflix"));

        assertEquals("Entertainment",
                categorisationService.categoriseTransaction("Spotify"));

        assertEquals("Entertainment",
                categorisationService.categoriseTransaction("Amazon Prime Video"));

        assertEquals("Entertainment",
                categorisationService.categoriseTransaction("Disney+"));
    }

    @Test
    void shouldCategoriseAmazonAsShopping() {
        assertEquals("Shopping",
                categorisationService.categoriseTransaction("Amazon"));

        assertEquals("Shopping",
                categorisationService.categoriseTransaction("Takealot"));
    }

    @Test
    void shouldCategoriseRestaurantsAndTakeaways() {
        assertEquals("Food & Dining",
                categorisationService.categoriseTransaction("KFC"));

        assertEquals("Food & Dining",
                categorisationService.categoriseTransaction("Nandos"));

        assertEquals("Food & Dining",
                categorisationService.categoriseTransaction("McDonalds"));
    }

    @Test
    void shouldCategoriseUtilities() {
        assertEquals("Utilities",
                categorisationService.categoriseTransaction("Eskom"));

        assertEquals("Utilities",
                categorisationService.categoriseTransaction("Electricity"));
    }

    @Test
    void shouldCategoriseTelecommunications() {
        assertEquals("Telecommunications",
                categorisationService.categoriseTransaction("Vodacom"));

        assertEquals("Telecommunications",
                categorisationService.categoriseTransaction("MTN"));
    }

    @Test
    void shouldCategoriseHealthcare() {
        assertEquals("Healthcare",
                categorisationService.categoriseTransaction("Clicks"));

        assertEquals("Healthcare",
                categorisationService.categoriseTransaction("Dis-Chem"));
    }

    @Test
    void shouldReturnOtherForUnknownMerchant() {
        assertEquals("Other",
                categorisationService.categoriseTransaction("Unknown Merchant"));
    }

    @Test
    void shouldHandleNullAndBlankMerchants() {
        assertEquals("Other",
                categorisationService.categoriseTransaction(null));

        assertEquals("Other",
                categorisationService.categoriseTransaction(""));

        assertEquals("Other",
                categorisationService.categoriseTransaction("   "));
    }

    @Test
    void shouldIgnoreCaseDifferences() {
        assertEquals("Groceries",
                categorisationService.categoriseTransaction("CHECKERS"));

        assertEquals("Transport",
                categorisationService.categoriseTransaction("uber"));

        assertEquals("Entertainment",
                categorisationService.categoriseTransaction("NETFLIX"));
    }
}
