---
pattern: dp-game
family: dp
difficulty: hard
leetcode: 1872
time: O(n)
space: O(n)
tags:
  - dp
---
# Stone Game VIII
[LC 1872](https://leetcode.com/problems/stone-game-viii/) · [[Game Theory DP]]

## Recognize
Two players alternate, must merge prefixes. Game-theory minimax with a hidden simplification.

## Insight
After any sequence of merges, the score the active player can take is **always a prefix sum** of the original array. Don't track the modified array — just the next prefix sum index.

## Approach
Precompute `pre[i]` = prefix sum.
`dp(i)` — max **score difference** for the current player choosing from `pre[i], pre[i+1], ..., pre[n−1]`.
- **Take `pre[i]`:** `pre[i] − dp(i+1)`
- **Skip:** `dp(i+1)`

Must take at least 2 stones → start from `dp(2)`. Last prefix sum must be taken (can't skip everything) → `dp(n−1) = pre[n−1]`.

Bottom-up: `dp[i] = max(dp[i+1], pre[i] − dp[i+1])` right-to-left.

## Pitfalls
- "Skip" is allowed for non-final indices, but **not** for the last (must take).
- Sign convention: `dp(i)` is the score *difference* (current minus opponent's optimal).
- Don't try to simulate merges — the prefix-sum invariant is the whole trick.

## Similar
[[Can I Win]]
