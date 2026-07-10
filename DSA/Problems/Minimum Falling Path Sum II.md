---
pattern: dp-grid
family: dp
difficulty: hard
leetcode: 1289
time: O(n²)
space: O(n)
tags:
  - dp
---
# Minimum Falling Path Sum II
[LC 1289](https://leetcode.com/problems/minimum-falling-path-sum-ii/) · [[Grid DP]]

## Recognize
Falling path through a square grid; next row must use a **different column**. Naive O(n³) — improve to O(n²).

## Insight
For each row, the next row's transition is "best of all columns except this one" → only the **two smallest values** in the previous row matter. Use the smallest unless it's the same column, then fall back to second-smallest.

## Approach
Bottom-up. For each row, compute its DP values, then extract `(s1, s1_idx, s2)` (two smallest values + index of smallest).
For row above, each cell `(i, j)` does:
- if `j != s1_idx`: `dp[i][j] = grid[i][j] + s1`
- else: `dp[i][j] = grid[i][j] + s2`

## Pitfalls
- Track two smallest of **DP values**, not raw grid values.
- Compute two-smallest **after** the row's DP is ready — that's why this fits naturally as bottom-up.
- Handle ties: when multiple columns share the smallest, second-smallest may equal smallest.

## Similar
[[Cherry Pickup II]]
