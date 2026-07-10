package DesignPatterns.Observer;

public interface StockObserver {
    void update(Stock stock, double oldPrice, double newPrice);
}
