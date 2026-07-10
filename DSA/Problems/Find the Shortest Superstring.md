---
pattern: dp-bitmask
family: dp
difficulty: hard
leetcode: 943
time: O(2^n · n²)
space: O(2^n · n)
tags:
  - dp
---
# Find the Shortest Superstring
[LC 943](https://leetcode.com/problems/find-the-shortest-superstring/) · [[Bitmask DP]]

## Recognize
Shortest string containing all given strings. `n ≤ 12` → bitmask TSP with overlap as edge weight + reconstruction.

## Insight
Cost to append `j` after `last` = `len[j] − overlap[last][j]`. Classic TSP with reconstruction via parent pointers.

## Approach
Precompute `overlap[i][j]` = longest suffix of `s[i]` matching prefix of `s[j]`.
`dp(mask, last)` — min total length to cover strings in `mask`, ending with string `last`.
Use `n` as sentinel for "no previous string" → arrays sized `[1 << n][n + 1]`.

Reconstruction: store `par[mask][last] = next_best_j` only when `ans` actually improves. Trace forward from `(0, n)` through `par`, appending `s[next].substr(overlap[last][next])` each step.

## Pitfalls
- Track parent pointers **only on improvement** — not every iteration.
- Sentinel `last = n` lets you start cleanly; otherwise you need a separate initial branch.
- 2¹² × 12 ≈ 50K states — comfortable.

## Similar
[[Smallest Sufficient Team]] · [[Shortest Path Visiting All Nodes]]
