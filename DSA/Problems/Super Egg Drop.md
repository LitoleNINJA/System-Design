---
pattern: dp-binsearch
family: dp
difficulty: hard
leetcode: 887
time: O(eggs·floors·log floors)
space: O(eggs·floors)
tags:
  - dp
---
# Super Egg Drop
[LC 887](https://leetcode.com/problems/super-egg-drop/) · [[DP - Overview]]

## Recognize
Find the critical floor in worst-case minimum moves, with `k` eggs and `n` floors.

## Insight
`dp(floors, eggs) = min over x of: 1 + max(dp(x−1, k−1), dp(floors−x, k))`. As `x` increases, `dp(x−1, k−1)` grows and `dp(floors−x, k)` shrinks → single crossover → binary search for optimal `x`.

## Approach
`dp(floors, eggs)` — min moves to find critical floor in worst case.
- Binary search `x ∈ [1, floors]` for the crossover where `dp(x−1, k−1) == dp(floors−x, k)`.
- Check both sides of the crossover (integer boundary) — `best_x` and `best_x − 1`.

Base: `floors == 0` → `0`; `eggs == 1` → `floors` (linear sweep).

## Pitfalls
- Brute O(floors²) over `x` is the naive trap — binary search is the speedup.
- Floor `x` is 1-indexed; subproblem sizes are `x−1` (below) and `floors−x` (above).
- Worst case is `max`, best strategy is `min` — don't mix up.

## Similar
[[Maximum Number of Events That Can Be Attended II]]
