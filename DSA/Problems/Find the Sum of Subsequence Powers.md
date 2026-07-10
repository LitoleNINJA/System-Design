---
pattern: dp-knapsack
family: dp
difficulty: hard
leetcode: 3098
time: O(n·k·D)
space: O(n·k·D)
tags:
  - dp
---
# Find the Sum of Subsequence Powers
[LC 3098](https://leetcode.com/problems/find-the-sum-of-subsequence-powers/) · [[Knapsack DP]]

## Recognize
Sum the "power" (min abs diff between any pair) over all subsequences of length `k`. Combinatorial explosion meets DP.

## Insight
Sort the array. Min abs diff in any subsequence is always between **consecutive picks** in sorted order — so the state's `d` dimension only ever takes one of `C(n, 2) ≈ 1225` values. Coordinate-compress those.

## Approach
Sort. Precompute all `O(n²)` pairwise diffs, sort + dedupe → `rev[id] = diff`, `dif[diff] = id`. Append INF sentinel.

`dp(i, k, d)` — sum of powers of all subsequences ending at index `i`, `k` picks remaining, current min-diff = `rev[d]`.
- For each `j > i`: `new_min = min(rev[d], a[j] − a[i])`, recurse with `dif[new_min]`.

Base: `k == 0` → `rev[d] % mod`.
Answer: `Σ recurse(i, k−1, dif[INF])` for each start index `i`.

## Pitfalls
- Don't mix compressed ids with actual diff values — decompress before comparing, recompress before recursing.
- INF sentinel is essential for the initial "no diff yet" state.
- O(n²) pairwise diffs is fine at n ≤ 50.

## Similar
[[Tallest Billboard]] · [[String Compression II]]
