package DesignPatterns.Strategy;

import java.util.List;
import java.util.Map;

public class ExactSplit implements SplitStrategy {

    private final Map<User, Double> exactAmounts;

    public ExactSplit(Map<User, Double> exactAmounts) {
        this.exactAmounts = Map.copyOf(exactAmounts);
    }

    @Override
    public void validate(Expense expense) {
        for (User u : expense.getParticipants()) {
            if (!exactAmounts.containsKey(u)) {
                throw new IllegalArgumentException("Missing exact amount for " + u);
            }
        }
        double sum = exactAmounts.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - expense.getTotal()) > 0.01) {
            throw new IllegalArgumentException(
                "Exact amounts must sum to total " + expense.getTotal() + ", got " + sum);
        }
    }

    @Override
    public List<Share> split(Expense expense) {
        validate(expense);
        return expense.getParticipants().stream()
                .map(u -> new Share(u, exactAmounts.get(u)))
                .toList();
    }
}
