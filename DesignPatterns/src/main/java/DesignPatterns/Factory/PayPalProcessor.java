package DesignPatterns.Factory;

public class PayPalProcessor implements PaymentProcessor{
    @Override
    public boolean validate(PaymentRequest request) {
        String paypalId = request.getDetail("paypalId");
        if(paypalId == null || !paypalId.contains("@")) {
            return false;
        }

        System.out.println("PayPal ID validated !");
        return true;
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        if(!validate(request)) {
            System.out.println("Payment validation failed.");
            return new PaymentResponse(PaymentResponse.Status.FAILED, null, "validation failed");
        }

        System.out.println("Payment using : PayPal");
        String transactionId = "PP-" + System.nanoTime();

        return new PaymentResponse(PaymentResponse.Status.SUCCESS, transactionId, "payment done");
    }

    @Override
    public void refund(String transactionId) {
        System.out.println("Refund using : PayPal");
    }
}
