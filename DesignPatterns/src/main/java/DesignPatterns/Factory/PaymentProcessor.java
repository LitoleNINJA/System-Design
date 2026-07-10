package DesignPatterns.Factory;

public interface PaymentProcessor {
    boolean validate(PaymentRequest request);
    PaymentResponse pay(PaymentRequest request);
    void refund(String transactionId);
}
