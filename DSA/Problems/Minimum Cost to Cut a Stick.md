---
pattern: dp-interval
family: dp
difficulty: hard
leetcode: 1547
time: O(c³)
space: O(c²)
tags:
  - dp
---
# Minimum Cost to Cut a Stick
[LC 1547](https://leetcode.com/problems/minimum-cost-to-cut-a-stick/) · [[Interval DP]]

## Recognize
Cut a stick at given positions. Each cut costs the current segment's length. Order is up to you — minimize total cost.

## Insight
Cuts are sparse (≤ 100), stick can be huge. Make **indices into the sorted cuts array (with sentinels 0 and n added)** the state, not raw positions. Then it's classic interval DP.

## Approach
Let `cuts` be sorted, with `0` and `n` prepended/appended. `dp(i, j)` — min cost to handle all cuts strictly between `cuts[i]` and `cuts[j]`.
```
dp(i, j) = min over k in (i, j) of: dp(i, k) + dp(k, j) + (cuts[j] − cuts[i])
```
Base: `j − i <= 1` → `0` (no cuts between adjacent boundaries).

## Pitfalls
- Loop must be **strictly** `i < k < j` — endpoint inclusion causes infinite recursion.
- Add boundary sentinels (0 and `n`) so all cuts have a well-defined enclosing segment.
- Indices into `cuts`, not raw positions — that's the size-reduction trick.

## Similar
[[Remove Boxes]] · [[Scramble String]]
