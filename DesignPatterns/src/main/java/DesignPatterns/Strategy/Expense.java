package DesignPatterns.Strategy;

import java.util.List;
import java.util.Objects;

public class Expense {
    private final double total;
    private final User payer;
    private final List<User> participants;
    private SplitStrategy splitStrategy;        // mutable — the swap point

    public Expense(double total, User payer, List<User> participants) {
        if (total <= 0)             throw new IllegalArgumentException("total must be > 0");
        if (participants.isEmpty()) throw new IllegalArgumentException("at least one participant required");
        this.total        = total;
        this.payer        = Objects.requireNonNull(payer);
        this.participants = List.copyOf(participants);
        this.splitStrategy = new EqualSplit();
    }

    public void setStrategy(SplitStrategy strategy) {
        this.splitStrategy = Objects.requireNonNull(strategy);
    }

    public List<Share> getShares() {
        return splitStrategy.split(this);
    }

    public double     getTotal()        { return total; }
    public User       getPayer()        { return payer; }
    public List<User> getParticipants() { return participants; }
}
