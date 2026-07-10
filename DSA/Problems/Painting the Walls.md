---
pattern: dp-knapsack
family: dp
difficulty: hard
leetcode: 2742
time: O(n²)
space: O(n²)
tags:
  - dp
---
# Painting the Walls
[LC 2742](https://leetcode.com/problems/painting-the-walls/) · [[Knapsack DP]]

## Recognize
Two workers (paid + free, free is instant), pick subset for paid worker, minimize cost. Constraint links the two workers' work.

## Insight
A paid painter taking wall `i` effectively covers `time[i] + 1` walls total — itself, plus `time[i]` free walls the free painter handles in parallel.
→ Reframe as min-cost knapsack: pick walls so `Σ (time[i] + 1) ≥ n`.

## Approach
`dp(i, rem)` — min cost considering walls `[i..n−1]`, `rem` walls still needing coverage.
- **Skip:** `dp(i+1, rem)`
- **Take:** `dp(i+1, max(0, rem − time[i] − 1)) + cost[i]`

## Pitfalls
- Cap `rem` at 0 (threshold) — without `max(0, ...)`, state blows up to `−n`.
- `rem == 0` returns `0`, not `INF` — once covered, no more cost needed.

## Similar
[[Profitable Schemes]] · [[Smallest Sufficient Team]]
