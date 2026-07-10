---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 1220
time: O(n)
space: O(n)
tags:
  - dp
---
# Count Vowels Permutation
[LC 1220](https://leetcode.com/problems/count-vowels-permutation/) · [[Counting DP]]

## Recognize
Count strings of length `n` using {a,e,i,o,u} with specific allowed transitions. State = last char.

## Insight
Encode allowed transitions in a table, then the transition is a clean loop instead of nested if-else.

## Approach
`dp(i, ch)` — number of strings of length `i` ending with `ch`.
```
dp(i, ch) = Σ dp(i−1, prev_ch) for prev_ch in allowed_predecessors[ch]
```
Base: `dp(1, ch) = 1` for each vowel. Answer: `Σ dp(n, ch)` over 5 vowels.

## Pitfalls
- Make sure direction is consistent — predecessors vs successors. Build the table once both ways or pick one.
- Mod every addition.

## Similar
[[Number of Music Playlists]]
