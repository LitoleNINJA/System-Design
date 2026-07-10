package DesignPatterns.Factory;

public class UpiProcessor implements PaymentProcessor{
    @Override
    public boolean validate(PaymentRequest request) {
        String upiId = request.getDetail("upiId");
        if(upiId == null || !upiId.contains("@")) {
            return false;
        }

        System.out.println("UPI ID validated !");
        return true;
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        if(!validate(request)) {
            System.out.println("Payment validation failed.");
            return new PaymentResponse(PaymentResponse.Status.FAILED, null, "validation failed");
        }

        System.out.println("Payment using : UPI");
        String transactionId = "UPI-" + System.nanoTime();

        return new PaymentResponse(PaymentResponse.Status.SUCCESS, transactionId, "payment done");
    }

    @Override
    public void refund(String transactionId) {
        System.out.println("Refund using : UPI");
    }
}
