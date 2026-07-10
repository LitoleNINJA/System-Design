package DesignPatterns.Observer;

public class EmailAlertObserver implements StockObserver{
    private final String email;

    public EmailAlertObserver(String email) {
        this.email = email;
    }

    @Override
    public void update(Stock stock, double oldPrice, double newPrice) {
        System.out.printf("[EMAIL] To %s: %s is now $%.2f\n", stock.getSymbol(), email, newPrice);
    }
}
