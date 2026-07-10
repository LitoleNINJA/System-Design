---
type: pattern
name: Bitmask DP
family: dp
tags: [pattern, dp]
---
# Bitmask DP

## Signature
State includes a subset / visited-set encoded as a bitmask. Use when n ≤ 20-ish.

## Recognize
- "Visit all", "cover all", "assign each to ..."
- n is small (≤ 16-20)
- Order doesn't matter inside the subset

## Canonical state / structure
`dp[mask]` or `dp[mask, last]` — best/count for the subset `mask`, optionally tracking the last-used element.

## Transition / steps
- **Add one element:** `new_mask = mask | (1 << i)`
- **Iterate over set bits** with `for (int m = mask; m; m &= m - 1) { int i = __builtin_ctz(m); ... }`
- **TSP form:** `dp[mask][last]` over all `(prev, last)` edges.

## Variants
- **Bitmask on the smaller side** — hats ≤ 40 but people ≤ 10 → mask over people, iterate over hats. (Different Hats)
- **BFS + bitmask** — shortest path visiting all nodes. State `(node, mask)`, push all `(i, 1<<i)` initially. (Shortest Path Visiting All Nodes)
- **TSP-style superstring** — `dp[mask][last]` with reconstruction via parent pointers. (Find the Shortest Superstring)
- **Game-theory mask** — turn and running total are derivable from mask. (Can I Win)
- **Multiset state instead of subset** — when items have few distinct equivalence classes and same-class items are interchangeable, key the memo on a **tuple of counts per class**, not a bitmask over indices. `2^n` collapses to multiset count, often by orders of magnitude. Memoize with `unordered_map<array<int,K>, int>` + custom hash. (Maximum Number of Groups Getting Fresh Donuts)

## Pitfalls
- Use `|` to add, **not `^`** (toggles, removes).
- Don't mutate `dp[mask]` before copying the candidate.
- Sentinel for "no previous" → `n` (extend `last` to `[0..n]`).
- Precompute per-item masks when independent of state (knapsack-style); recompute on demand when state-dependent (Stickers-style).

## Problems
- [[Smallest Sufficient Team]]
- [[Shortest Path Visiting All Nodes]]
- [[Can I Win]]
- [[Number of Ways to Wear Different Hats to Each Person]]
- [[Find the Shortest Superstring]]
- [[Maximum Number of Groups Getting Fresh Donuts]]
