package DesignPatterns.Strategy;

import java.util.List;
import java.util.Map;

/**
 * Client of the Strategy pattern.
 *
 * The defining demonstration: a SINGLE Expense object is passed through
 * THREE different split rules via {@code expense.setStrategy(...)}. That
 * runtime swap is what makes this Strategy and not Factory.
 */
public class BillSplitterDemo {

    public static void main(String[] args) {
        User alice   = new User("u1", "Alice");
        User bob     = new User("u2", "Bob");
        User charlie = new User("u3", "Charlie");

        Expense expense = new Expense(300.00, alice, List.of(alice, bob, charlie));

        System.out.println("=== EQUAL split ===");
        expense.setStrategy(new EqualSplit());
        printShares(expense);

        System.out.println("\n=== EXACT split (50, 100, 150) ===");
        expense.setStrategy(new ExactSplit(Map.of(
            alice,    50.0,
            bob,     100.0,
            charlie, 150.0
        )));
        printShares(expense);

        System.out.println("\n=== PERCENT split (20%, 30%, 50%) ===");
        expense.setStrategy(new PercentSplit(Map.of(
            alice,   20.0,
            bob,     30.0,
            charlie, 50.0
        )));
        printShares(expense);

        System.out.println("\n=== Validation failure (percentages sum to 90%) ===");
        try {
            expense.setStrategy(new PercentSplit(Map.of(
                alice,   20.0,
                bob,     30.0,
                charlie, 40.0
            )));
            expense.getShares();
            System.out.println("BUG: expected an IllegalArgumentException but none was thrown");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
    }

    private static void printShares(Expense e) {
        for (Share s : e.getShares()) {
            System.out.printf("%-7s owes $%.2f%n", s.user(), s.amount());
        }
    }
}
