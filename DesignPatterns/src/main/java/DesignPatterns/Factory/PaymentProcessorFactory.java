package DesignPatterns.Factory;

class PaymentProcessorFactory {
    public static PaymentProcessor create(PaymentType type) throws IllegalArgumentException {
        return switch (type) {
            case CREDIT_CARD -> new CreditCardProcessor();
            case UPI -> new UpiProcessor();
            case PAYPAL -> new PayPalProcessor();
        };
    }
}
