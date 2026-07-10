package DesignPatterns.Observer;

/**
 * Test driver / demo for the Stock Market Alerts exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting classes in this same
 * folder/package so this file compiles and runs:
 *
 *   1. StockObserver.java          — interface: update(Stock, oldPrice, newPrice)
 *   2. Stock.java                  — subject: symbol, price, observers list,
 *                                    addObserver, removeObserver, setPrice
 *   3. PriceLogger.java            — silent audit observer (no ctor args)
 *   4. EmailAlertObserver.java     — ctor: String email
 *   5. SMSAlertObserver.java       — ctor: String phoneNumber
 *   6. ThresholdAlertObserver.java — ctor: double threshold
 *                                    fires only when oldPrice >= threshold && newPrice < threshold
 *
 * Until all six exist you will get "cannot find symbol" errors — that's the
 * spec talking. Each error tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of Observer: ONE Stock object has FOUR
 * heterogeneous subscribers, each reacting differently to the same price
 * change. We then unsubscribe one and prove it stops receiving updates.
 * The Stock class never imports a single concrete observer class — that's
 * the open/closed property at work.
 */
public class StockMarketDemo {

    public static void main(String[] args) {
        Stock apple = new Stock("AAPL", 150.00);

        StockObserver email     = new EmailAlertObserver("alice@example.com");
        StockObserver sms       = new SMSAlertObserver("+1-555-0100");
        StockObserver logger    = new PriceLogger();
        StockObserver threshold = new ThresholdAlertObserver(145.00);

        apple.subscribe(email);
        apple.subscribe(sms);
        apple.subscribe(logger);
        apple.subscribe(threshold);

        // Requirement #5: adding the same observer twice should NOT duplicate notifications.
        apple.subscribe(email);

        System.out.println("=== Initial subscribers: Email(alice), SMS(+1-555-0100), Logger, Threshold(below $145) ===");

        System.out.println("\n--- Price change: $150.00 -> $155.00 ---");
        apple.setPrice(155.00);   // threshold should NOT fire (no crossing)

        System.out.println("\n--- Price change: $155.00 -> $140.00 ---");
        apple.setPrice(140.00);   // threshold SHOULD fire (crosses below 145)

        System.out.println("\n=== Unsubscribing Email observer ===");
        apple.unsubscribe(email);

        // Requirement #6: removing a non-subscribed observer is a no-op.
        apple.unsubscribe(new PriceLogger());

        System.out.println("\n--- Price change: $140.00 -> $142.00 ---");
        apple.setPrice(142.00);   // Email gone; threshold doesn't refire (no fresh crossing)
    }
}
