# Exercise — Bill Splitter

> Strategy pattern · LLD practice
> Interview-style problem in the algomaster.io exercise format.

---

## Problem Statement

You are building the bill-splitting feature of an expense-tracking app like **Splitwise**. When a group of friends spends money together, the app must split the expense among the participants according to one of several rules:

- **Equal** — split the total evenly among all *N* participants.
- **Exact** — each participant owes a specific absolute amount, supplied up front. The amounts must sum to the total.
- **Percentage** — each participant owes a specific percentage of the total. The percentages must sum to **100%**.

The app should be **easy to extend** when new split types appear later (e.g., *"by shares"* — Alice gets 2 shares, Bob gets 1).

Design and implement this system using the **Strategy** pattern. Your design must let the user **change the split rule on an existing expense at runtime** — without recreating the expense.

---

## Requirements

1. Define a `SplitStrategy` interface that every split rule implements:
   - `List<Share> split(Expense expense)` — compute each participant's share.
   - `void validate(Expense expense)` — throw `IllegalArgumentException` if the strategy's per-participant data is inconsistent with the expense (wrong total, missing participant, etc.).

2. Implement three concrete strategies: `EqualSplit`, `ExactSplit`, `PercentSplit`.

3. **Per-strategy input data must live on the strategy itself, NOT on the Expense.** The Expense must know nothing about exact amounts or percentages — that's the strategy's business.

4. The `Expense` class is the **context** — it holds the chosen strategy as a *field* and exposes a `getShares()` method that delegates to the strategy. The strategy must be **swappable** via a `setStrategy(...)` method.

5. Client code (your `main`) must depend **only** on the `Expense` class and the `SplitStrategy` abstraction — *never* on a concrete strategy except at the point of construction.

6. Adding `ByShareSplit` tomorrow must touch **only** the new class. No edits to `Expense`, no edits to existing strategies.

7. Validation must throw a clear `IllegalArgumentException` — don't silently return wrong numbers.

---

## Class Hints

```
class    User                            // id, name; equals/hashCode by id
record   Share(User user, double amount) // result of one participant's slice

interface SplitStrategy {
    List<Share> split   (Expense expense);
    void        validate(Expense expense);
}

class EqualSplit   implements SplitStrategy   // no extra data needed
class ExactSplit   implements SplitStrategy   // holds Map<User, Double> exactAmounts
class PercentSplit implements SplitStrategy   // holds Map<User, Double> percentages

class Expense {                        // the CONTEXT
    double         total;
    User           payer;
    List<User>     participants;
    SplitStrategy  strategy;           // ← THE swap point
    void           setStrategy(SplitStrategy s);
    List<Share>    getShares();        // delegates to strategy.split(this)
}

class BillSplitterDemo { public static void main(String[] args) { ... } }
```

---

## Expected Output

For an expense of **$300** paid by Alice with participants **[Alice, Bob, Charlie]**:

```
=== EQUAL split ===
Alice   owes $100.00
Bob     owes $100.00
Charlie owes $100.00

=== EXACT split (50, 100, 150) ===
Alice   owes $50.00
Bob     owes $100.00
Charlie owes $150.00

=== PERCENT split (20%, 30%, 50%) ===
Alice   owes $60.00
Bob     owes $90.00
Charlie owes $150.00

=== Validation failure (percentages sum to 90%) ===
java.lang.IllegalArgumentException: Percentages must sum to 100, got 90.0
```

> 🔑 **Key observation:** all three splits operate on the **same `Expense` object**. Only the strategy was swapped via `setStrategy(...)`. *That* is what makes this Strategy and not Factory.

---

## What the Interviewer is Looking For

- **Strategy held as a field on the context** (Expense), not chosen via a static factory call. This is the property that distinguishes Strategy from Factory.
- **Strategy is swappable mid-life** — the demo proves it on the same Expense object.
- **Per-strategy data lives on the strategy.** Expense doesn't carry a `Map<User, Double>` of any kind.
- **Validation lives on the strategy.** Each strategy knows its own constraints (sum-to-total vs sum-to-100).
- **Open/Closed:** adding `ByShareSplit` modifies *nothing* existing.
- **Strategy is pure** — `split()` returns new data, doesn't mutate the Expense.
- **Be ready for the probe:** *"How is this different from Factory?"* (Hint: Factory hands you an object and walks away; Strategy lives inside the Expense and gets called repeatedly.)

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. `User`, `Share` — value objects first.
2. `SplitStrategy` interface — just the two method signatures.
3. `EqualSplit` — easiest (no per-strategy data, near-trivial validation).
4. `Expense` context — focus on `setStrategy` + `getShares`.
5. ✅ **Run it now** with one strategy. Don't add the others until this works.
6. `ExactSplit` and `PercentSplit` — copy the recipe from `EqualSplit`. Each takes a `Map<User, Double>` in its constructor and validates it.
7. `BillSplitterDemo` — exercise all three on the **same** Expense object. If swapping feels awkward, your design has a smell.

**Suggested folder for your attempt:** `DesignPatterns/Strategy/MyAttempt/` (or any sibling of this exercise folder — keep your code separate from the reference).

A reference implementation lives alongside this README. Don't peek until you've attempted, or until you're stuck for >15 minutes. The teaching companion `DESIGN_NOTES.md` covers Strategy vs Factory vs State, where per-strategy data should live, the SOLID scorecard, and a C++ sketch — read it after your attempt.

---

## Files in this Exercise

| File | Role |
|------|------|
| `User.java`              | Value object (id, name) |
| `Share.java`             | Value object (user, amount) — output of split |
| `SplitStrategy.java`     | The Strategy interface |
| `EqualSplit.java`        | Concrete strategy |
| `ExactSplit.java`        | Concrete strategy |
| `PercentSplit.java`      | Concrete strategy |
| `Expense.java`           | The Context — holds the strategy as a field |
| `BillSplitterDemo.java`  | Client / `main` |
| `DESIGN_NOTES.md`        | Teaching companion: Strategy vs Factory/State, SOLID, C++ sketch, interview pitch |
