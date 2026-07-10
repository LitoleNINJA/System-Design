# Exercise — Stock Market Alerts

> Observer pattern · LLD practice
> Frequently-asked LLD interview problem. Variants: stock alerts, YouTube channel notifications, news feed updates, auction bidding alerts.

---

## Problem Statement

You are building the alerting system of a stock-trading app. When a stock's price changes, **multiple subscribers** must be notified — each may want updates via a different channel (email, SMS, mobile push, internal log) or under a different condition (only fire when the price drops below a threshold).

The `Stock` class must **not** know who its subscribers are or how they choose to be notified. It should just say *"my price changed"*, and every registered subscriber should react. Subscribers can join or leave at any time, and adding new subscriber types (Slack, Discord, fraud detector, regulatory audit log) must not require any change to `Stock`.

Design and implement this system using the **Observer** pattern.

---

## Requirements

1. Define a `StockObserver` interface that every subscriber implements:
   - `update(Stock stock, double oldPrice, double newPrice)`
2. Define a `Stock` class — the **subject**:
   - Holds `symbol`, current `price`, and a list of `StockObserver`s.
   - `addObserver(StockObserver o)` — registers a subscriber.
   - `removeObserver(StockObserver o)` — unregisters a subscriber.
   - `setPrice(double newPrice)` — updates the price **and** notifies every observer.
3. Implement these concrete observers:
   - `EmailAlertObserver` — takes a user email in its constructor; prints a "Sending email…" line on update.
   - `SMSAlertObserver` — takes a phone number; prints an "Sending SMS…" line.
   - `PriceLogger` — silent audit observer; prints `[LOG] AAPL: $X -> $Y` on every change.
   - `ThresholdAlertObserver` — takes a threshold price; **only** fires an alert when the price *crosses* below the threshold (i.e., `oldPrice >= threshold && newPrice < threshold`). Tests that observers can have their own state and conditions.
4. **Subscribers can subscribe or unsubscribe at any time** during the stock's lifetime.
5. Adding the same observer instance twice must **not** cause duplicate notifications.
6. Removing an observer that wasn't subscribed must be a **no-op**, not an error.
7. `Stock` must hold observers as `List<StockObserver>` (the interface) — it must **never** import or reference a concrete observer class.
8. The demo `main` calls only `Stock`'s public API and the observer constructors — proving the subject doesn't know what kinds of observers exist.

---

## Class Hints

```
interface StockObserver {
    void update(Stock stock, double oldPrice, double newPrice);
}

class Stock {                                // the SUBJECT
    String  symbol;
    double  price;
    List<StockObserver> observers;
    void   addObserver   (StockObserver o);
    void   removeObserver(StockObserver o);
    void   setPrice      (double newPrice);  // ← updates price AND notifies
    String getSymbol();
    double getPrice();
}

class EmailAlertObserver     implements StockObserver   // ctor: email
class SMSAlertObserver       implements StockObserver   // ctor: phone number
class PriceLogger            implements StockObserver   // no ctor args
class ThresholdAlertObserver implements StockObserver   // ctor: double threshold

class StockMarketDemo { public static void main(String[] args) { ... } }
```

---

## Expected Output

```
=== Initial subscribers: Email(alice), SMS(+1-555-0100), Logger, Threshold(below $145) ===

--- Price change: $150.00 -> $155.00 ---
[Email] To alice@example.com: AAPL is now $155.00
[SMS]   To +1-555-0100: AAPL is now $155.00
[LOG]   AAPL: $150.00 -> $155.00

--- Price change: $155.00 -> $140.00 ---
[Email] To alice@example.com: AAPL is now $140.00
[SMS]   To +1-555-0100: AAPL is now $140.00
[LOG]   AAPL: $155.00 -> $140.00
[ALERT] AAPL dropped below threshold $145.00 (now $140.00)

=== Unsubscribing Email observer ===

--- Price change: $140.00 -> $142.00 ---
[SMS]   To +1-555-0100: AAPL is now $142.00
[LOG]   AAPL: $140.00 -> $142.00
```

Note three things in this output:
1. **All observers fire on every price change** — broadcast semantics.
2. **`ThresholdAlertObserver` only fires when the price *crosses* the threshold from above** (not on every change while below). Compare run #2 (fires) vs the implicit fact that if we set price to $138 next, it would NOT fire again.
3. **After unsubscribing Email**, only SMS and Logger react. The unsubscribe took effect.

---

## What the Interviewer is Looking For

- **Subject holds the observer list as `List<Interface>`** — never imports a concrete observer. That's the open/closed test.
- **Observer interface is narrow** (one method). If you find yourself adding methods to it, you're conflating concerns.
- **Subscribe/unsubscribe work at runtime**, not just at construction.
- **Adding a new observer type** is one new class — zero edits to `Stock` or other observers.
- **Push vs pull model:** we're using **push** (subject pushes `(stock, old, new)` to `update`). Be ready to discuss when pull is preferable (when observers care about different fields and you don't want to push everything).
- **Memory leak awareness:** in long-running systems, observers that aren't unsubscribed will live as long as the subject — a real bug. Be ready to mention `WeakReference` or explicit lifecycle management when probed.
- **Thread safety:** if asked, mention that iterating over the observer list while another thread subscribes/unsubscribes would `ConcurrentModificationException`. Production code uses `CopyOnWriteArrayList` or a synchronized snapshot.
- **Reentrancy / loops:** an observer that calls `setPrice` inside its own `update` would loop forever. Real systems guard against this.

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. `StockObserver.java` — interface, just the signature.
2. `Stock.java` — symbol, price, observers list, addObserver, removeObserver, setPrice. Use `ArrayList<StockObserver>`.
3. `PriceLogger.java` — easiest observer, no state, no constructor args. Just prints the change.
4. ✅ **Run the demo with only Logger subscribed.** If `[LOG]` appears, the core wiring works.
5. `EmailAlertObserver.java` — has a constructor field (the email).
6. `SMSAlertObserver.java` — same recipe as Email.
7. `ThresholdAlertObserver.java` — slightly trickier: only fires when `oldPrice >= threshold && newPrice < threshold`.
8. Run the full demo. Output should match exactly (modulo small formatting choices).

**A hint that will save you confusion:**
- Inside `setPrice`, save the *old* price into a local variable **before** updating `this.price`. Pass both old and new to `update`. Otherwise observers can't compare.
- Inside `addObserver`, check `if (!observers.contains(o)) observers.add(o);` to satisfy requirement #5.

---

## Files in this Exercise

| File | Role |
|------|------|
| `Stock.java`                  | The Subject — holds observers, notifies on change |
| `StockObserver.java`          | The Observer interface |
| `EmailAlertObserver.java`     | Concrete observer — email channel |
| `SMSAlertObserver.java`       | Concrete observer — SMS channel |
| `PriceLogger.java`            | Concrete observer — silent audit log |
| `ThresholdAlertObserver.java` | Concrete observer with state + condition |
| `StockMarketDemo.java`        | Client / `main` — **provided as the test contract** |
