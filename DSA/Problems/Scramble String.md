---
pattern: dp-interval
family: dp
difficulty: hard
leetcode: 87
time: O(n⁴)
space: O(n³)
tags:
  - dp
---
# Scramble String
[LC 87](https://leetcode.com/problems/scramble-string/) · [[Interval DP]]

## Recognize
Recursively swap children of substrings. Determine if `s2` is a scramble of `s1`. Both substrings always equal length → 3D state, not 4D.

## Insight
Both halves always have **equal length** → state is `(i1, i2, len)`, not `(i1, j1, i2, j2)`. Then split at every `k` and try both orderings.

## Approach
`dp(i1, i2, len)` — is `s1[i1..i1+len)` a scramble of `s2[i2..i2+len)`?
For split length `k = 1..len−1`:
- **Keep order:** `dp(i1, i2, k) && dp(i1+k, i2+k, len−k)`
- **Swap:** `dp(i1, i2+len−k, k) && dp(i1+k, i2, len−k)`

Base: `len == 1` → `s1[i1] == s2[i2]`.

Quick reject: if character frequencies of both substrings don't match, `false`.

## Pitfalls
- Swap-side offset: right half of `s2` starts at `i2 + len − k`, **not** `i2 + k`.
- Always quick-reject by frequency — without it, exponential blowup.
- 30 × 30 × 30 ≈ 27K states, comfortable.

## Similar
[[Regular Expression Matching]] · [[Remove Boxes]]
