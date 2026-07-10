package DesignPatterns.Observer;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private final String symbol;
    private double price;
    private final List<StockObserver> observers;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
        this.observers = new ArrayList<>();
    }
    public String getSymbol() {return this.symbol;}
    public double getPrice() {return this.price;}

    public void subscribe(StockObserver observer) {
        if(observers.contains(observer))
            return;
        this.observers.add(observer);
    }

    public void unsubscribe(StockObserver observer) {
        this.observers.remove(observer);
    }

    public void setPrice(double price) {
        double oldPrice = this.price;
        this.price = price;
        for(StockObserver observer : this.observers) {
            observer.update(this, oldPrice, price);
        }
    }
}
