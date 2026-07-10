package DesignPatterns.Strategy;

import java.util.List;

public interface SplitStrategy {
    void validate(Expense expense);
    List<Share> split(Expense expense);
}
