package DesignPatterns.Factory;

import java.util.HashMap;

public class PaymentResponse {
    public enum Status {SUCCESS, FAILED, INVALID};
    private final Status status;
    private final String transactionId;
    private final String message;

    public PaymentResponse(Status status, String transactionId, String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }

    public Status getStatus()          { return status; }
    public String getTransactionId()   { return transactionId; }
    public String getMessage()         { return message; }
}
