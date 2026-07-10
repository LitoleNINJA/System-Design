---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 920
time: O(goal·n)
space: O(goal·n)
tags:
  - dp
---
# Number of Music Playlists
[LC 920](https://leetcode.com/problems/number-of-music-playlists/) · [[Counting DP]]

## Recognize
"Count playlists" of length `goal` from `n` songs, every song used ≥ once, no song repeats within `k`.

## Insight
Don't track *which* songs were used — only *how many* distinct songs so far. Symmetry collapses the state.

## Approach
`dp(i, j)` — number of playlists of length `i` using `j` distinct songs.
- **New song:** `dp(i−1, j−1) · (n − j + 1)` — any of the unused songs.
- **Repeat:** `dp(i−1, j) · max(0, j − k)` — any of the `j` used, minus the last `k`.

Answer: `dp(goal, n)`. Base: `dp(0, 0) = 1`.

## Pitfalls
- The gap rule becomes a *multiplier*, not a *state*.
- `max(0, j − k)` — when fewer than `k` distinct used, no repeat is allowed.
- Modulo wraps every product and sum.

## Similar
[[Count Vowels Permutation]] · [[K Inverse Pairs Array]]
