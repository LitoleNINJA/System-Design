---
type: pattern
name: Knapsack DP
family: dp
tags: [pattern, dp]
---
# Knapsack DP

## Signature
Choose a subset of items, optimize value/cost under a capacity constraint.

## Recognize
- "Subset of items", "0/1 choice per item"
- Capacity / budget / count constraint
- Maximize value or minimize cost

## Canonical state / structure
`dp(i, cap)` — best from items `[i..n-1]` with `cap` remaining.

## Transition / steps
- **Skip:** `dp(i+1, cap)`
- **Take:** `dp(i+1, cap - weight[i]) + value[i]` (if fits)

## Variants
- **0/1** — each item once.
- **Unbounded** — stay at `i`: `dp(i, cap - w[i])`.
- **Threshold cap** — once dim exceeds threshold, extra doesn't matter → cap. (Profitable Schemes, Painting the Walls)
- **Diff trick** — two groups with related sums → track `diff = sum1 − sum2`. (Tallest Billboard)
- **Multi-dim capacity** — multiple independent constraints. (Profitable Schemes: people + profit)
- **Transformed** — reframe items/capacity. (Painting the Walls)
- **Compressed state** — distinct realizable values << range → coordinate-compress. (Subsequence Powers)

## Pitfalls
- `cap < 0` not handled → guard before recursion, or `max(0, cap − w[i])` if threshold-capping.
- "Best so far" vs "best at exact base" — be precise about base meaning.
- `INT_MAX` overflow on min-cost knapsack — use `LLONG_MAX/2` or check before adding.

## Problems
- [[Painting the Walls]]
- [[Profitable Schemes]]
- [[Tallest Billboard]]
- [[Smallest Sufficient Team]]
- [[Find the Sum of Subsequence Powers]]
- [[String Compression II]]
- [[Number of Ways to Form a Target String Given a Dictionary]]
- [[Maximum Number of Events That Can Be Attended II]]
