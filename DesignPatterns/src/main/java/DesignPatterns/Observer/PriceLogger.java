package DesignPatterns.Observer;

public class PriceLogger implements StockObserver {
    @Override
    public void update(Stock stock, double oldPrice, double newPrice) {
        System.out.printf("[LOG]   %s: $%.2f -> $%.2f\n", stock.getSymbol(), oldPrice, newPrice);
    }
}
