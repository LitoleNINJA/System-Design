package DesignPatterns.Factory;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean validate(PaymentRequest request) {
        String cardNo = request.getDetail("cardNumber");
        if(cardNo == null || cardNo.length() < 12) {
            return false;
        }

        System.out.println("Card Number validated !");
        return true;
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        if(!validate(request)) {
            System.out.println("Payment validation failed.");
            return new PaymentResponse(PaymentResponse.Status.FAILED, null, "validation failed");
        }

        System.out.println("Payment using : Credit Card");
        String transactionId = "CC-" + System.nanoTime();

        return new PaymentResponse(PaymentResponse.Status.SUCCESS, transactionId, "payment done");
    }

    @Override
    public void refund(String transactionId) {
        System.out.println("Refund using : Credit Card");
    }
}
