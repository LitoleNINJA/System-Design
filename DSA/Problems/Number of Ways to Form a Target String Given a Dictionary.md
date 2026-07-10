---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 1639
time: O(m·t)
space: O(m·t)
tags:
  - dp
---
# Number of Ways to Form a Target String Given a Dictionary
[LC 1639](https://leetcode.com/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/) · [[Counting DP]]

## Recognize
Build `target` by picking one char per column (left-to-right, columns strictly increasing). Many dictionary words of same length — count compositions.

## Insight
The *words themselves* don't matter — only `freq[col][char]` does. Compress the dictionary to a frequency table once, then it's a clean knapsack-style count over columns.

## Approach
Precompute `freq[k][c]` = how many words have char `c` at column `k`.
`dp(i, k)` — number of ways to build `target[i..]` using columns `k..m-1`.
- **Skip column:** `dp(i, k+1)`
- **Take column:** `dp(i+1, k+1) · freq[k][target[i]]`

Base: `i == n` → `1`; `k == m && i < n` → `0`.

## Pitfalls
- Columns are strictly increasing — once you take col `k`, never revisit `≤ k`.
- Skip vs take is independent of the target char; the multiplier handles "no match" via `freq = 0`.

## Similar
[[Number of Music Playlists]] · [[Profitable Schemes]]
