package DesignPatterns.Observer;

public class SMSAlertObserver implements StockObserver {
    private final String phone;

    public SMSAlertObserver(String phone) {
        this.phone = phone;
    }

    @Override
    public void update(Stock stock, double oldPrice, double newPrice) {
        System.out.printf("[SMS]   To %s: %s is now $%.2f\n", stock.getSymbol(), phone, newPrice);
    }
}
