---
pattern: dp-partition
family: dp
difficulty: medium
leetcode: 1043
time: O(n·k)
space: O(n)
tags:
  - dp
---
# Partition Array for Maximum Sum
[LC 1043](https://leetcode.com/problems/partition-array-for-maximum-sum/) · [[Partition DP]]

## Recognize
Partition `arr` into contiguous groups of length ≤ `k`. Each element becomes the group's max. Maximize total sum.

## Insight
Same as Min Difficulty Job Schedule structurally — partition + per-segment max. Cost is **incremental max** as you grow the segment leftward from index `i`.

## Approach
`dp(i)` — max sum from `arr[0..i)`.
For partition length `j = 1..k` ending at `i−1`:
```
dp(i) = max over j of: dp(i−j) + j · max(arr[i−j..i−1])
```
Track `mx` incrementally inside the `j` loop.

Base: `dp(0) = 0`. Answer: `dp(n)`.

## Pitfalls
- `mx` lives **inside** the inner loop, not outside — it grows as the partition extends leftward.
- Off-by-one on bounds: `i − j >= 0`, `j <= k`.
- The multiplier is `j × mx`, not `mx` (segment becomes all `mx`s).

## Similar
[[Minimum Difficulty of a Job Schedule]] · [[Palindrome Partitioning III]] · [[String Compression II]]
