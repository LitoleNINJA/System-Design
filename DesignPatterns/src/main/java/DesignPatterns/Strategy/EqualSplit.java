package DesignPatterns.Strategy;

import java.util.List;

public class EqualSplit implements SplitStrategy {

    @Override
    public void validate(Expense expense) {
        if (expense.getParticipants().isEmpty()) {
            throw new IllegalArgumentException("Cannot split among zero participants");
        }
    }

    @Override
    public List<Share> split(Expense expense) {
        validate(expense);
        double perUser = expense.getTotal() / expense.getParticipants().size();
        return expense.getParticipants().stream()
                .map(u -> new Share(u, perUser))
                .toList();
    }
}
