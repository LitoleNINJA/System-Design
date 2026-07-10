---
pattern: dp-bitmask
family: dp
difficulty: hard
leetcode: 1125
time: O(2^s · n)
space: O(2^s)
tags:
  - dp
---
# Smallest Sufficient Team
[LC 1125](https://leetcode.com/problems/smallest-sufficient-team/) · [[Bitmask DP]]

## Recognize
≤ 16 required skills → state as bitmask. Each bit = one skill covered. Classic "set cover with smallest count".

## Insight
Precompute `personSkillMask[p]` once. Each person becomes a `|=` operation on the current skill mask — turns set cover into a clean state transition.

## Approach
`dp[mask]` — smallest team (vector of person indices) covering `mask`.

For each `mask` in `0..(1<<s)−1`, for each person `p`:
```
new_mask = mask | personSkillMask[p]
if (dp[mask] + {p}).size() < dp[new_mask].size():
    dp[new_mask] = dp[mask] + {p}
```
Final answer: `dp[(1<<s) − 1]`.

## Pitfalls
- Use `|` to add skills, **not `^`** (which would toggle/remove).
- Don't mutate `dp[mask]` before copying — build the candidate vector first, then compare and assign.
- Precomputing person→mask works here (0/1 knapsack-style); doesn't apply when picks depend on dynamic state.
- Initialize `dp[0]` to empty vector, all others to "infinity-size" sentinel.

## Similar
[[Find the Shortest Superstring]] · [[Number of Ways to Wear Different Hats to Each Person]] · [[Shortest Path Visiting All Nodes]]
