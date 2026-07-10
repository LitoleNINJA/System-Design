package DesignPatterns.State;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test driver / demo for the Vending Machine exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting classes in this same
 * folder/package so this file compiles and runs:
 *
 *   1. Item.java                — value object: code, name, priceCents, stock
 *   2. VendingMachineState.java — interface with insertCoin, selectItem,
 *                                 dispense, refund (each takes VendingMachine)
 *   3. VendingMachine.java      — context: holds inventory, balance,
 *                                 selectedCode, current state. Public actions
 *                                 delegate one-line to state.X(this, ...).
 *   4. IdleState.java           — initial state, only insertCoin transitions
 *   5. HasMoneyState.java       — money in, awaiting selection
 *   6. DispensingState.java     — transient state; dispenses then transitions
 *   7. SoldOutState.java        — terminal state; rejects coin/select
 *
 * Until all seven exist you'll get "cannot find symbol" errors — each error
 * tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of State: the SAME user actions
 * (insertCoin, selectItem, refund) produce DIFFERENT behavior depending on
 * which state the machine is in. And critically, the VendingMachine class
 * itself contains zero `if (state == ...)` checks — every action is a
 * one-line delegate to the current state.
 */
public class VendingMachineDemo {

    public static void main(String[] args) {
        // Inventory: 2x Coke @ 75c, 1x Chips @ 100c.
        // LinkedHashMap so iteration order is predictable across runs.
        Map<String, VendingMachine.Item> inventory = new LinkedHashMap<>();
        inventory.put("A1", new VendingMachine.Item("A1", "Coke",  75,  2));
        inventory.put("B1", new VendingMachine.Item("B1", "Chips", 100, 1));

        VendingMachine machine = new VendingMachine(inventory);

        System.out.println("=== Inventory: Coke[A1]=2 @ 75c, Chips[B1]=1 @ 100c ===");

        // Action 1 — interact while Idle. Selecting before paying must be rejected.
        System.out.println("\n--- Try selecting without coins ---");
        machine.selectItem("A1");

        // Action 2 — happy path with overpay. Insert 100c, buy Coke (75c), get 25c change.
        System.out.println("\n--- Insert 100c, select A1 (Coke @ 75c) ---");
        machine.insertCoin(100);
        machine.selectItem("A1");

        // Action 3 — multi-coin top-up. Insert 50c twice, then buy Chips.
        // After this, Chips inventory hits 0 — DispensingState should print a
        // "last unit of B1" line and transition us back to Idle (not SoldOutState
        // for the whole machine yet — Coke still has stock).
        System.out.println("\n--- Insert 50c (not enough), then 50c more, select B1 (Chips @ 100c) ---");
        machine.insertCoin(50);
        machine.insertCoin(50);
        machine.selectItem("B1");

        // Action 4 — try to buy a sold-out item. HasMoneyState should reject the
        // selection AND auto-refund the inserted coins (or expose refund to do so).
        System.out.println("\n--- Try buying Chips again (sold out) ---");
        machine.insertCoin(50);
        machine.selectItem("B1");
        machine.refund();

        // Action 5 — buy the last remaining Coke. After this, the WHOLE
        // machine should enter SoldOutState (every item count is zero).
        System.out.println("\n--- Buy last Coke ---");
        machine.insertCoin(75);
        machine.selectItem("A1");

        // Action 6 — verify SoldOutState rejects coin insertion.
        System.out.println("\n--- SoldOutState rejects new coins ---");
        machine.insertCoin(100);
    }
}
