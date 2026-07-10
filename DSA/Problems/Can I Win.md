---
pattern: dp-bitmask
family: dp
difficulty: medium
leetcode: 464
time: O(2^n · n)
space: O(2^n)
tags:
  - dp
---
# Can I Win
[LC 464](https://leetcode.com/problems/can-i-win/) · [[Bitmask DP]] · [[Game Theory DP]]

## Recognize
Two-player game, pick from `1..n` without replacement, first to reach `desiredTotal` wins. Returns whether current player (Alice) can force a win.

## Insight
State is just `mask` — turn and running total are **derivable** from which numbers are picked. No extra dim needed.

## Approach
`dp(mask)` — does the player to move win from this position?
- For each unpicked `i`:
  - If `total + i >= desiredTotal` → immediate win.
  - Else if `!dp(mask | (1 << i))` → opponent loses from there → current wins.
- If no winning move found, return `false`.

Early prune: if `sum(1..n) < desiredTotal`, no one can ever reach it → `false`.

## Pitfalls
- `2²⁰ ≈ 1M` states — fits, comfortable.
- The "opponent loses" check is `!dp(...)` — that's the minimax flip.
- Don't forget the early-prune; without it, deep recursion on impossible cases.

## Similar
[[Stone Game VIII]] · [[Smallest Sufficient Team]]
