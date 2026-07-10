package DesignPatterns.Observer;

public class ThresholdAlertObserver implements StockObserver {
    private final double threshold;

    public ThresholdAlertObserver(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void update(Stock stock, double oldPrice, double newPrice) {
        if(oldPrice >= threshold && newPrice < threshold) {
            System.out.printf("[ALERT] %s dropped below threshold $%.2f (now $%.2f)%n", stock.getSymbol(), threshold, newPrice);
        }
    }
}
