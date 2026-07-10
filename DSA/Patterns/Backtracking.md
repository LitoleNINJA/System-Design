---
type: pattern
name: Backtracking
family: array
tags: [pattern, backtracking]
---
# Backtracking

## Signature
Explore a decision tree by trying each choice, recursing, and undoing the choice on return. The right tool when the search space is small enough — due to depth or branching bounds — that brute force with pruning beats DP, especially when "DP state" would need a subset or path and explode.

## Recognize
- Small n, or constraints that bound depth/branching tightly (often implicitly — read the limits carefully)
- "Find all..." / "max/min over all paths/subsets/permutations"
- State that would be unwieldy as a memo key (visited subset, current path)
- Looks DP-shaped but bitmask state space is too big (e.g., 2^50)

## Canonical state / structure
```
dfs(state):
    if base case:
        record / update answer
        // don't always return — there may still be more to explore
    for choice in options:
        if valid:
            apply(choice)          // mutate shared state
            dfs(next_state)
            undo(choice)           // restore — symmetric with apply
```

## Transition / steps
- Pass mutable state (visited set, current path) **by reference** — apply on enter, undo on exit.
- Local "did I mutate?" flag per call when entries are conditional (e.g., add-to-visited only on first encounter), so undo is also conditional.
- Prune aggressively: bound on remaining budget, dominated branches, ordering choices to fail fast.

## Variants
- **Subset / combination enumeration** — include-or-exclude per element.
- **Permutation enumeration** — pick next from remaining; swap-in-place is a common trick.
- **Path search with revisits allowed** — visited set tracks "value already collected", not "do not enter". Backtracking is the call-that-added-removes. (Maximum Path Quality of a Graph)
- **Constraint satisfaction** — N-queens, Sudoku, partial assignments with feasibility checks.

## Pitfalls
- Forgetting to undo state on backtrack → dirty state leaks into sibling branches.
- Passing mutable state by value when you wrote explicit undo code — undo is dead, and you pay a copy per call.
- Over-pruning: forbidding revisits when the problem allows them; pruning on a bound that isn't tight.
- Recording the answer too eagerly — make sure the base case matches problem semantics (e.g., "must end at start node", not "anywhere").
- Reaching for DP when constraints make brute force viable. If 2^n is too big but the search tree is small (deep × narrow), DFS dominates.

## Problems
- [[Maximum Path Quality of a Graph]]
