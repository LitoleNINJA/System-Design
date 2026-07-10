---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 446
time: O(n²)
space: O(n²)
tags:
  - dp
---
# Arithmetic Slices II - Subsequence
[LC 446](https://leetcode.com/problems/arithmetic-slices-ii-subsequence/) · [[Counting DP]]

## Recognize
Count arithmetic subsequences (length ≥ 3) in an array. Subsequence + arithmetic → state on (last index, common difference).

## Insight
`diff` can be huge or negative → can't be an array index. Use `map<long long, int>` per index. Pair `(j, i)` extends every AP ending at `j` with the same `diff`.

## Approach
`dp[i][diff]` — number of arithmetic subsequences ending at index `i` with common difference `diff` (length ≥ 2).
For each pair `(j, i)` with `j < i`:
```
diff = a[i] − a[j]
ans   += dp[j][diff]              // these have length ≥ 3 — valid answer
dp[i][diff] += dp[j][diff] + 1    // +1 is the new pair (j, i), length 2
```

## Pitfalls
- `diff` is `long long` — int overflow on extreme values.
- Pairs of length 2 don't count toward the answer, only setup future extensions.
- Don't reset `dp[i]` between `j` iterations — they accumulate.

## Similar
[[Russian Doll Envelopes]]
