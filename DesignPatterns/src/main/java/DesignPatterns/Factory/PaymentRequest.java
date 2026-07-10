package DesignPatterns.Factory;

import java.util.HashMap;

public class PaymentRequest {
    public enum Currency{USD, INR}
    private final Currency currency;

    private final double amount;
    private final String userId;
    private final HashMap<String, String> details;

    public PaymentRequest(double amount, Currency currency, String userId, HashMap<String, String> details) {
        this.amount = amount;
        this.currency = currency;
        this.userId = userId;
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }
    public String getUser() {
        return userId;
    }
    public Currency getCurrency() {
        return currency;
    }
    public String getDetail(String key) {
        return details.get(key);
    }
}
