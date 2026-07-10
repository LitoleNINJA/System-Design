---
pattern: dp-counting
family: dp
difficulty: hard
leetcode: 1269
time: O(steps²)
space: O(steps)
tags:
  - dp
---
# Number of Ways to Stay in the Same Place After Some Steps
[LC 1269](https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/) · [[Counting DP]] · [[Grid DP]]

## Recognize
Count ways to walk left/right/stay on a 1D array and return to position 0 in exactly `steps` moves.

## Insight
Position can never exceed `min(arrLen − 1, steps / 2)` — beyond half the budget you can't get back in time. Tight cap on the state dim.

## Approach
`dp(remaining, pos)` — number of ways back to 0 from `pos` with `remaining` steps.
```
dp(r, pos) = dp(r−1, pos−1) + dp(r−1, pos) + dp(r−1, pos+1)
```
Cap `pos` at `min(arrLen − 1, steps / 2)`. Base: `r == 0 && pos == 0` → `1`.

## Pitfalls
- Without the cap, state space blows up to `steps × arrLen` which can be 500 × 10⁶.
- Bounds: `pos − 1 >= 0`, `pos + 1 < arrLen`.
- Mod on each addition.

## Similar
[[Number of Ways of Cutting a Pizza]]
