---
pattern: dp-math
family: dp
difficulty: hard
leetcode: 818
time: O(t log t)
space: O(t)
tags:
  - dp
---
# Race Car
[LC 818](https://leetcode.com/problems/race-car/) · [[DP - Overview]]

## Recognize
Position-and-speed state, exponential move structure (`A` doubles speed), needs reverse to undershoot/overshoot a target. Greedy fails — optimal often requires intentional overshoot.

## Insight
After `n` consecutive `A`s from position 0, speed 1: **position = 2ⁿ − 1**, speed = 2ⁿ.
At any state, only **two meaningful** choices: smallest `n` that overshoots `t`, or `n−1` (undershoot) followed by reverse + partial A's.

## Approach
`dp(t)` — min instructions to reach position `t` from origin.

1. **Exact:** if `2ⁿ − 1 == t` → answer is `n`.
2. **Overshoot:** smallest `n` with `2ⁿ − 1 > t`. Use `n` A's + `R`, solve remainder:
   `n + 1 + dp((2ⁿ − 1) − t)`
3. **Undershoot:** use `n−1` A's (lands at `2^(n−1) − 1 < t`), `R`, `m` A's back, `R` again:
   `(n − 1) + 1 + m + 1 + dp(t − (2^(n−1) − 1) + (2ᵐ − 1))` for `m ∈ [0, n−2]`.

Take the min. Base: `dp(0) = 0`.

## Pitfalls
- Only **two meaningful** `n` candidates per target — enumerating all powers blows up.
- Undershoot's inner `m` is `[0, n−2]`; `m = n−1` collapses to "exact" and is already handled.
- `2ⁿ − 1` overflows fast — `t ≤ 10⁴` means `n ≤ 14`, comfortable in int.

## Similar
[[Super Egg Drop]]
