---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 879
time: O(g·n·p)
space: O(g·n·p)
tags:
  - dp
---
# Profitable Schemes
[LC 879](https://leetcode.com/problems/profitable-schemes/) · [[Counting DP]] · [[Knapsack DP]]

## Recognize
Subset of crimes with two constraints (people, min profit). Count subsets, not optimize. Classic multi-dim counting knapsack.

## Insight
"At least `minProfit`" is a *threshold*. Once profit ≥ `minProfit`, the exact value is irrelevant — cap the state dimension.

## Approach
`dp(i, n, p)` — number of subsets from crimes `[i..end]` using `n` people and `p` profit so far.
- **Skip:** `dp(i+1, n, p)`
- **Take** (if fits): `dp(i+1, n−g[i], min(minProfit, p + p[i]))`

Base: `i == numGroups` → `1` if `p >= minProfit`, else `0`.

## Pitfalls
- Threshold cap on `p` is the whole trick — without it, state blows up.
- "Number of schemes" is a count, so add the two branches; no min/max.
- Modulo on every addition.

## Similar
[[Painting the Walls]] · [[Number of Ways to Form a Target String Given a Dictionary]]
