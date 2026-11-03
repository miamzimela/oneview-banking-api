
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

        // Restaurants and Takeaways
        // Uber Eats must be checked before Uber.
        if (name.contains("uber eats")
                || name.contains("ubereats")
                || name.contains("mcdonald")
                || name.contains("kfc")
                || name.contains("nando")
                || name.contains("steers")
                || name.contains("deb onairs")
                || name.contains("debonairs")
                || name.contains("spur")
                || name.contains("romans pizza")) {
            return "Food & Dining";
        }

        // Transport
        if (name.contains("uber")
                || name.contains("bolt")
                || name.contains("engen")
                || name.contains("shell")
                || name.contains("sasol")
                || name.contains("bp")
                || name.contains("gautrain")) {
            return "Transport";
        }

        // Entertainment
        // Amazon Prime Video is separate from Amazon Shopping.
        if (name.contains("netflix")
                || name.contains("spotify")
                || name.contains("prime video")
                || name.contains("amazon prime")
                || name.contains("disney+")
                || name.contains("disney plus")) {
            return "Entertainment";
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
        // Use "rain mobile" instead of "rain" to
        // avoid incorrectly categorising Gautrain.
        if (name.contains("vodacom")
                || name.contains("mtn")
                || name.contains("telkom")
                || name.contains("cell c")
                || name.contains("rain mobile")) {
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
