---
type: pattern
name: Partition DP
family: dp
tags: [pattern, dp]
---
# Partition DP

## Signature
Split a fixed-order sequence into contiguous groups (often exactly `k`). Cost is sum of per-group costs. Minimize / maximize the total.

## Recognize
- "Split into `k` contiguous segments"
- Per-segment cost computable from substring
- Every element must belong to some partition (no skip)

## Canonical state / structure
`dp(i, k)` — best value for `s[i..n-1]` with `k` partitions remaining.

## Transition / steps
Sweep endpoint `j` from `i` to `n−k`:
```
dp(i, k) = min/max over j of: dp(j+1, k-1) + cost(i, j)
```
If `cost(i, j)` is incremental (running max, running palindrome cost), update inside the `j` loop in O(1) instead of recomputing.

## Variants
- **Cost as running max/min** — update incrementally inside `j` loop. (Min Difficulty Job Schedule, Partition Array for Max Sum)
- **Cost precomputed** — palindrome-changes-needed in O(n²), then O(n²·k) main DP. (Palindrome Partitioning III)
- **No `k` constraint** — partition into any number of groups, `dp(i)` instead of `dp(i, k)`.

## Pitfalls
- **No skip transition** — every element belongs to some partition. Different from knapsack.
- Off-by-one on `j` bound: must leave at least `k-1` elements for remaining partitions, so `j ≤ n−k`.
- Base: `i == n && k == 0` → `0`; `i == n || k == 0` → `INF` (or `−INF` for max). Don't merge them.

## Problems
- [[Minimum Difficulty of a Job Schedule]]
- [[Palindrome Partitioning III]]
- [[Partition Array for Maximum Sum]]
- [[Minimum Cost to Cut a Stick]]
