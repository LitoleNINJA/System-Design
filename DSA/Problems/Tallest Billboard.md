---
pattern: dp-knapsack
family: dp
difficulty: hard
leetcode: 956
time: O(n·S)
space: O(n·S)
tags:
  - dp
---
# Tallest Billboard
[LC 956](https://leetcode.com/problems/tallest-billboard/) · [[Knapsack DP]]

## Recognize
Partition rods into two equal-sum groups, maximize that sum. Naive `dp(i, sum1, sum2)` is too big.

## Insight
Only `diff = sum1 − sum2` matters; final answer requires `diff == 0`. State collapses one dimension.

## Approach
`dp(i, diff)` — max height of **group1**, using rods `[0..i)`, with `group1 − group2 = diff`.
- **Skip:** `dp(i+1, diff)`
- **Group1:** `dp(i+1, diff + a[i]) + a[i]`  (track group1's height — only grows when adding to group1)
- **Group2:** `dp(i+1, diff − a[i])`

Answer: `dp(n, 0)`. Offset `diff` by `5000` for array indexing, or use a hashmap.

## Pitfalls
- `+ a[i]` is only on the group1 branch — that's the height you're tracking.
- `diff` can be negative — offset for array index, or hashmap.
- Sum bound is 5000 (constraint), so offset by 5000.

## Similar
[[Smallest Sufficient Team]] · [[Find the Sum of Subsequence Powers]]
