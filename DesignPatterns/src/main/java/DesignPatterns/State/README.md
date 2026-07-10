# Exercise — Vending Machine

> State pattern · LLD practice
> *The* canonical State interview problem. Asked at Amazon, Atlassian, Microsoft, Uber, and most product cos with an LLD round. Direct cousin of Strategy — interviewers often probe the difference, so doing this right after Strategy locks in the distinction.

---

## Problem Statement

Design a snack-and-drink **vending machine**. The machine accepts coins, dispenses items, and transitions between distinct *modes of behavior* depending on what's happening:

- **Idle** — no coins inserted, no item selected. The machine is waiting.
- **HasMoney** — the user has inserted enough money but hasn't yet selected an item.
- **Dispensing** — the user has selected an item and the machine is dispensing it.
- **SoldOut** — there's no inventory left; the machine refuses to accept coins or selections.

The same user action means **different things in different states**. Pressing "select Coke" while in `Idle` should reject (no money). Pressing it while in `HasMoney` should dispense. Pressing it while in `SoldOut` should reject. Same input, different behavior — that's the heart of the State pattern.

Your task: implement this **without sprinkling `if (state == ...) else if (state == ...)` checks all over the `VendingMachine` class**. Each state should be its own class that encapsulates the behavior valid in that state and is responsible for transitioning the machine to the next state.

---

## Requirements

1. Define a `VendingMachineState` interface (or abstract class) with the four user-action methods:
   - `insertCoin(VendingMachine machine, int amount)`
   - `selectItem(VendingMachine machine, String itemCode)`
   - `dispense(VendingMachine machine)`
   - `refund(VendingMachine machine)`
2. Implement four concrete states: `IdleState`, `HasMoneyState`, `DispensingState`, `SoldOutState`. Each enforces what's valid:
   - `IdleState.selectItem(...)` should reject ("Insert coin first").
   - `HasMoneyState.insertCoin(...)` accepts more coins (top-up) but doesn't transition.
   - `DispensingState` rejects all inputs (machine is busy).
   - `SoldOutState` rejects coin insertion and selection; `refund` still works for any pending balance.
3. The `VendingMachine` class is the **context**:
   - Holds inventory, current balance, and the current `VendingMachineState`.
   - Delegates every public action (`insertCoin`, `selectItem`, etc.) to the current state.
   - Exposes `setState(VendingMachineState s)` so a state can transition the machine when its action completes.
4. **State transitions live inside the state classes** — `VendingMachine` itself contains **no** `if`/`switch` on state. Each state knows which state to transition to next.
5. The machine must reach `SoldOutState` automatically when the last unit of inventory is dispensed.
6. Refund must return the user's balance and transition the machine back to `Idle` (unless we're in `SoldOut` and there's no balance — then stay in `SoldOut`).
7. Adding a new state tomorrow (e.g., `MaintenanceState`) must require **zero edits** to existing states or to `VendingMachine`.

---

## Class Hints

```
interface VendingMachineState {
    void insertCoin (VendingMachine m, int amount);
    void selectItem (VendingMachine m, String itemCode);
    void dispense   (VendingMachine m);
    void refund     (VendingMachine m);
}

class IdleState       implements VendingMachineState   // no money yet
class HasMoneyState   implements VendingMachineState   // money in, awaiting selection
class DispensingState implements VendingMachineState   // currently dispensing
class SoldOutState    implements VendingMachineState   // no inventory left

class Item {
    String code;       // e.g., "A1"
    String name;       // e.g., "Coke"
    int    price;      // in cents — avoid double for money
    int    stock;      // remaining units
}

class VendingMachine {                                 // the CONTEXT
    Map<String, Item>  inventory;
    int                balance;                        // current inserted balance, in cents
    String             selectedCode;                   // tracked for the dispense step
    VendingMachineState state;                         // ← THE swap point

    void setState     (VendingMachineState s);
    void insertCoin   (int amount)                     { state.insertCoin (this, amount); }
    void selectItem   (String code)                    { state.selectItem (this, code); }
    void dispense     ()                               { state.dispense   (this); }
    void refund       ()                               { state.refund     (this); }

    // helpers each state will call
    int                 getBalance();
    void                addBalance(int amount);
    int                 deductBalance(int amount);     // returns refund/change
    Item                getItem(String code);
    void                consumeOne(String code);
    boolean             isAllSoldOut();
    String              getSelectedCode();
    void                setSelectedCode(String code);
}

class VendingMachineDemo { public static void main(String[] args) { ... } }
```

> 💡 **Currency tip:** use `int cents` instead of `double dollars` everywhere. Floating-point arithmetic loses pennies — interviewers will probe this. `int` is the safe, idiomatic choice for money in interview-grade code.

---

## Expected Output

```
=== Inventory: Coke[A1]=2 @ 75c, Chips[B1]=1 @ 100c ===

--- Try selecting without coins ---
[IdleState] Insert coin first

--- Insert 100c, select A1 (Coke @ 75c) ---
[IdleState] Accepted 100c. Balance=100c
[HasMoneyState] Selected A1 (Coke). Dispensing...
[DispensingState] Dispensed Coke. Returning change 25c. Balance=0c

--- Insert 50c (not enough), then 50c more, select B1 (Chips @ 100c) ---
[IdleState] Accepted 50c. Balance=50c
[HasMoneyState] Accepted 50c. Balance=100c
[HasMoneyState] Selected B1 (Chips). Dispensing...
[DispensingState] Dispensed Chips. Returning change 0c. Balance=0c
[DispensingState] Last unit of Chips dispensed — entering SoldOut for B1

--- Try buying Chips again (sold out) ---
[IdleState] Accepted 50c. Balance=50c
[HasMoneyState] B1 (Chips) is sold out
[HasMoneyState] Refunding 50c. Balance=0c

--- Buy last Coke ---
[IdleState] Accepted 75c. Balance=75c
[HasMoneyState] Selected A1 (Coke). Dispensing...
[DispensingState] Dispensed Coke. Returning change 0c. Balance=0c
[DispensingState] All items sold out — entering SoldOutState

--- SoldOutState rejects new coins ---
[SoldOutState] Machine sold out — coin rejected
```

---

## What the Interviewer is Looking For

- **No `if`/`switch` on state in `VendingMachine`.** Every action is a one-line delegate to the current state. If your `VendingMachine` has more than ~10 lines of action logic, you're doing it wrong.
- **Each state is a named class** with explicit transition logic. The state machine is *visible* in the code — you can read `IdleState.insertCoin` and immediately see *"this transitions us to HasMoneyState."*
- **States transition the context, not vice versa.** State classes call `machine.setState(...)`. The machine is passive, the states are active.
- **Open/Closed:** adding `MaintenanceState` is one new class — no edits to existing states or to the machine.
- **Be ready for the State vs Strategy probe.** See the section below.

---

## State vs Strategy — *the* probe to expect

Both patterns share the same skeleton (interface, multiple implementations, swappable at runtime). They differ on three axes:

| Axis | State | Strategy |
|---|---|---|
| **Who decides to swap?** | The current state itself transitions the context to the next state. *State knows about other states.* | The client sets the strategy. *Strategy is unaware of other strategies.* |
| **What changes?** | The *valid actions* and the *meaning* of an action change. `selectItem` is rejected in Idle, valid in HasMoney. | The *algorithm* changes, but inputs/outputs stay the same. EqualSplit and PercentSplit both produce shares for any expense. |
| **Mental model** | Finite state machine — a directed graph of legal transitions. | Plug-in algorithms — pick any one, they're peers. |

**One-line answer:** *"State is for finite state machines where transitions are part of the design; Strategy is for interchangeable algorithms with no transitions between them."*

The dead giveaway in code: **a State implementation calls `context.setState(new OtherState())`.** A Strategy implementation never calls `context.setStrategy(...)`.

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. `Item.java` — value object with code, name, price (in cents!), stock.
2. `VendingMachineState.java` — interface, just the four method signatures.
3. `VendingMachine.java` — context skeleton. Constructor takes inventory map and sets `state = new IdleState()`. Public methods delegate. Add the helpers (`addBalance`, `deductBalance`, `consumeOne`, `isAllSoldOut`).
4. `IdleState.java` — only `insertCoin` does meaningful work (transitions to HasMoneyState). Everything else prints a rejection.
5. ✅ **Run the demo** — verify "select without coins" → "[IdleState] Insert coin first."
6. `HasMoneyState.java` — accepts more coins, handles selectItem (transitions to DispensingState), handles refund (back to Idle).
7. `DispensingState.java` — does the actual work: deducts balance, returns change, decrements inventory, decides next state (SoldOut if depleted, else Idle).
8. `SoldOutState.java` — rejects coin insert and selection; allows refund.

A pitfall to watch: **`DispensingState` is a "transient" state.** It does work and immediately transitions out — it never sits idle. Some candidates skip it and fold the dispense logic into `HasMoneyState.selectItem`. That works but interviewers ding it for hiding the "I'm currently dispensing" reality (which in real hardware *is* a measurable state where the motor is running).

---

## Files in this Exercise

| File | Role |
|------|------|
| `Item.java`                  | Value object: code, name, price (cents), stock |
| `VendingMachineState.java`   | The State interface |
| `IdleState.java`             | Concrete state — waiting for coins |
| `HasMoneyState.java`         | Concrete state — money in, awaiting selection |
| `DispensingState.java`       | Concrete state — dispensing the selected item |
| `SoldOutState.java`          | Concrete state — no inventory left |
| `VendingMachine.java`        | The Context — holds the current state + helpers |
| `VendingMachineDemo.java`    | Client / `main` — **provided as the test contract** |
