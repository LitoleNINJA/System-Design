---
pattern: dp-partition
family: dp
difficulty: hard
leetcode: 1278
time: O(n²·k)
space: O(n²)
tags:
  - dp
---
# Palindrome Partitioning III
[LC 1278](https://leetcode.com/problems/palindrome-partitioning-iii/) · [[Partition DP]]

## Recognize
Split `s` into exactly `k` contiguous segments. Cost = total chars changed to make each segment a palindrome. Minimize.

## Insight
Two DPs: precompute palindrome-changes-needed `cost[i][j]` (two-pointer, O(n²)), then standard partition DP.

## Approach
`cost[i][j]` — min changes to make `s[i..j]` a palindrome (sweep both ends, count mismatches).
`dp(i, k)` — min cost for `s[i..n-1]` with `k` partitions remaining.
Sweep endpoint `j` from `i` to `n−1`:
```
dp(i, k) = min over j of: dp(j+1, k−1) + cost[i][j]
```
Base: `i == n && k == 0` → `0`; `i == n || k == 0` → `INF`.

## Pitfalls
- No "skip" — every char belongs to a partition (this is partition DP, not knapsack).
- Don't merge the two failing base cases — they have different semantics.
- Cost precomputation must be O(n²), not naive O(n³).

## Similar
[[Partition Array for Maximum Sum]] · [[Minimum Difficulty of a Job Schedule]]
