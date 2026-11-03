package com.oneview.banking.service;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TransactionCategorisationService {

    public String categoriseTransaction(String merchant) {

        if (merchant == null || merchant.isBlank()) {
            return "Other";
        }

        String name = merchant.toLowerCase(Locale.ROOT).trim();

        // Groceries
        if (name.contains("checkers")
                || name.contains("shoprite")
                || name.contains("pick 'n pay")
                || name.contains("pnp")
                || name.contains("woolworths")
                || name.contains("spar")) {
            return "Groceries";
        }

        // Food delivery
        // Must appear before Transport to distinguish Uber Eats from Uber.
        if (name.contains("uber eats")) {
            return "Food & Dining";
        }

        // Transport
        if (name.contains("uber")
                || name.contains("bolt")
                || name.contains("engen")
                || name.contains("shell")
                || name.contains("sasol")
                || name.contains("bp")) {
            return "Transport";
        }

        // Entertainment
        // Must appear before Shopping to distinguish Amazon Prime from Amazon.
        if (name.contains("netflix")
                || name.contains("spotify")
                || name.contains("prime video")
                || name.contains("amazon prime")
                || name.contains("disney+")
                || name.contains("disney plus")) {
            return "Entertainment";
        }

        // Restaurants and Takeaways
        if (name.contains("mcdonald")
                || name.contains("kfc")
                || name.contains("nando")
                || name.contains("steers")
                || name.contains("debonairs")
                || name.contains("spur")
                || name.contains("romans pizza")) {
            return "Food & Dining";
        }

        // Shopping
        if (name.contains("takealot")
                || name.contains("amazon")
                || name.contains("mr price")
                || name.contains("edgars")
                || name.contains("jet")
                || name.contains("bash")
                || name.contains("shein")) {
            return "Shopping";
        }

        // Utilities
        if (name.contains("eskom")
                || name.contains("electricity")
                || name.contains("water")
                || name.contains("city power")
                || name.contains("municipality")) {
            return "Utilities";
        }

        // Telecommunications
        if (name.contains("vodacom")
                || name.contains("mtn")
                || name.contains("telkom")
                || name.contains("cell c")
                || name.contains("rain")) {
            return "Telecommunications";
        }

        // Healthcare
        if (name.contains("clicks")
                || name.contains("dis-chem")
                || name.contains("pharmacy")
                || name.contains("mediclinic")
                || name.contains("netcare")) {
            return "Healthcare";
        }

        // Default category
        return "Other";
    }
}
