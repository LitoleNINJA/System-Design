package DesignPatterns.Strategy;

import java.util.List;
import java.util.Map;

public class PercentSplit implements SplitStrategy {

    private final Map<User, Double> percentages;

    public PercentSplit(Map<User, Double> percentages) {
        this.percentages = Map.copyOf(percentages);
    }

    @Override
    public void validate(Expense expense) {
        for (User u : expense.getParticipants()) {
            if (!percentages.containsKey(u)) {
                throw new IllegalArgumentException("Missing percentage for " + u);
            }
        }
        double sum = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100, got " + sum);
        }
    }

    @Override
    public List<Share> split(Expense expense) {
        validate(expense);
        return expense.getParticipants().stream()
                .map(u -> new Share(u, expense.getTotal() * percentages.get(u) / 100.0))
                .toList();
    }
}
