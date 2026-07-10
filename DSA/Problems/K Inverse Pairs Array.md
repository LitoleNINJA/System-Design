---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 629
time: O(n·k)
space: O(n·k)
tags:
  - dp
---
# K Inverse Pairs Array
[LC 629](https://leetcode.com/problems/k-inverse-pairs-array/) · [[Counting DP]] · [[Permutation DP]]

## Recognize
Count permutations of `1..n` with exactly `k` inversions. Classic insertion DP.

## Insight
Insert element `i` into a permutation of `1..i−1`. Placing it at position-from-end `p ∈ [0, i−1]` creates exactly `p` new inversions. Sum over all positions → sliding window sum, collapsable with prefix sums.

## Approach
`dp(i, k) = Σ dp(i−1, k−j) for j in 0..i−1`

Prefix-sum optimization:
```
dp[i][k] = pre[k+1] − pre[max(0, k−i+1)]   // pre = prefix sums of dp[i−1]
```

Base: `dp[0][0] = 1`. Answer: `dp[n][k]`.

## Pitfalls
- Negative modulo from subtraction: `((a − b) % mod + mod) % mod`.
- Window endpoints: lower bound is `max(0, k−i+1)`, upper is `k`.
- Without prefix sums, naive is O(n·k·n) → TLE.

## Similar
[[Number of Ways to Rearrange Sticks With K Sticks Visible]]
