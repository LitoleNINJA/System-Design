---
pattern: dp-knapsack
family: dp
difficulty: hard
leetcode: 1531
time: O(n·k·26·n)
space: hashmap
tags:
  - dp
---
# String Compression II
[LC 1531](https://leetcode.com/problems/string-compression-ii/) · [[Knapsack DP]]

## Recognize
Delete up to `k` chars to minimize the length of the run-length encoding. RLE is non-local — kept chars merge with the previous run.

## Insight
State needs the **last kept char** and **its current run length**, because compression cost transitions discontinuously at run lengths 1→2, 9→10, 99→100. State space is sparse → top-down with hashmap.

## Approach
`dp(i, k, ch, len)` — min encoded length for `s[i..n−1]`, `k` deletions left, last kept char `ch` with current run length `len`.
- **Delete `s[i]`:** `dp(i+1, k−1, ch, len)` (if `k > 0`)
- **Keep, same char:** `dp(i+1, k, ch, len+1) + threshold_cost`
- **Keep, different char:** `dp(i+1, k, s[i], 1) + 1` (close out the old run, count it; +1 for the new char itself)

`threshold_cost` is `1` only at len = 1→2, 9→10, 99→100; else `0`.

## Pitfalls
- Sentinel for `ch` must not collide with valid chars — use 26, not 0.
- Sparse state → hashmap memo, not 4D array.
- Don't forget to *count* the closed-out previous run when switching char.

## Similar
[[Partition Array for Maximum Sum]]
