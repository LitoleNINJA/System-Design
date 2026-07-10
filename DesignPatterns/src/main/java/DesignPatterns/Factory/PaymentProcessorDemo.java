package DesignPatterns.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Client of the Factory Method pattern.
 *
 * Notice what this class does NOT import:
 *   CreditCardProcessor, PayPalProcessor, UpiProcessor.
 *
 * It depends only on the abstraction (PaymentProcessor) and the factory.
 * That's the Dependency Inversion Principle in action — adding a new
 * payment method tomorrow does not require a single edit here.
 */
public class PaymentProcessorDemo {

    public static void main(String[] args) {

        System.out.println("=== 1. Credit Card payment ===");
        runPayment(
            PaymentType.CREDIT_CARD,
            buildRequest(129.99, PaymentRequest.Currency.USD, "buyer-1",
                Map.of("cardNumber", "4111111111111111"))
        );

        System.out.println("\n=== 2. PayPal payment ===");
        runPayment(
            PaymentType.PAYPAL,
            buildRequest(75.50, PaymentRequest.Currency.USD, "buyer-2",
                Map.of("paypalId", "buyer@example.com"))
        );

        System.out.println("\n=== 3. UPI payment ===");
        runPayment(
            PaymentType.UPI,
            buildRequest(499.00, PaymentRequest.Currency.INR, "buyer-3",
                Map.of("upiId", "ritwik@okhdfc"))
        );

        System.out.println("\n=== 4. Validation failure (missing card number) ===");
        runPayment(
            PaymentType.CREDIT_CARD,
            buildRequest(50.00, PaymentRequest.Currency.USD, "buyer-4",
                Map.of() /* no cardNumber detail */)
        );

        System.out.println("\n=== 5. Refund a previous transaction ===");
        PaymentProcessor cc = PaymentProcessorFactory.create(PaymentType.CREDIT_CARD);
        cc.refund("CC-1700000001");
    }

    /** The whole demo flows through this single method — proof of polymorphism. */
    private static void runPayment(PaymentType type, PaymentRequest request) {
        PaymentProcessor processor = PaymentProcessorFactory.create(type);
        PaymentResponse response = processor.pay(request);
        printResponse(response);
    }

    private static PaymentRequest buildRequest(double amount,
                                               PaymentRequest.Currency currency,
                                               String userId,
                                               Map<String, String> extras) {
        HashMap<String, String> details = new HashMap<>(extras);
        return new PaymentRequest(amount, currency, userId, details);
    }

    private static void printResponse(PaymentResponse r) {
        System.out.println(
            "Result -> status=" + r.getStatus() +
            ", txn="            + r.getTransactionId() +
            ", msg="            + r.getMessage()
        );
    }
}
