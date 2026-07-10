---
pattern: dp-grid
family: dp
difficulty: hard
leetcode: 1463
time: O(n·m²)
space: O(n·m²)
tags:
  - dp
---
# Cherry Pickup II
[LC 1463](https://leetcode.com/problems/cherry-pickup-ii/) · [[Grid DP]]

## Recognize
Two robots descend a grid simultaneously, collect cherries, maximize total. Each can move down-left / down / down-right.

## Insight
Two agents on the same row at each step → state `(row, j1, j2)`. The "same step" sync collapses what could be two independent DPs into one.

## Approach
`dp(i, j1, j2)` — max cherries from row `i` onward, robots at columns `j1`, `j2`.
- Add `grid[i][j1]` (+ `grid[i][j2]` if `j1 != j2`).
- Try all 9 next-position pairs: `(d1, d2) ∈ {−1, 0, 1}²`, recurse `dp(i+1, j1+d1, j2+d2)`.

Base: `i == rows` → `0`. Answer: `dp(0, 0, cols−1)`.

## Pitfalls
- `j1 == j2` — count the cell **once**.
- Bounds: skip transitions where either robot leaves the grid.
- 9-direction loop, not 3 — both robots move independently per step.

## Similar
[[Minimum Falling Path Sum II]]
