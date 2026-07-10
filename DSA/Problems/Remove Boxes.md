---
pattern: dp-interval
family: dp
difficulty: hard
leetcode: 546
time: O(n⁴)
space: O(n³)
tags:
  - dp
---
# Remove Boxes
[LC 546](https://leetcode.com/problems/remove-boxes/) · [[Interval DP]]

## Recognize
Score `k²` for removing `k` consecutive same-color boxes. Plain `dp(i, j)` fails — boxes outside `[i, j]` can later combine with boxes inside, breaking the assumption that the subproblem is self-contained.

## Insight
Carry "attached chain from the left" as a third dimension:
> `dp(i, j, k)` — max score from `boxes[i..j]` with `k` boxes of color `boxes[i]` already attached on the left.

That `k` is the info from outside the interval that affects what happens inside.

**Pattern recognition:** when standard interval DP fails, ask *"is there info from outside the interval that affects decisions inside?"* — that's the extra state.

## Approach
Two choices for `boxes[i]`:
1. **Remove now:** `dp(i+1, j, 0) + (k+1)²` — the `k+1` includes attached + current.
2. **Save for a future match.** For each `m ∈ [i+1, j]` with `boxes[m] == boxes[i]`:
   - Clear the middle: `dp(i+1, m−1, 0)`
   - Combine `boxes[i]` with `boxes[m]`: `dp(m, j, k+1)`

Total: `dp(i+1, m−1, 0) + dp(m, j, k+1)`. Take the max over both choices.

## Pitfalls
- Score is `(k+1)²`, not `k²` — `k` is *already-attached*, plus the current `boxes[i]`.
- States: 100 × 100 × 100 = 10⁶ — fits, but recursion depth can hit limits.
- Don't forget the "remove now" branch — easy to only iterate `m` and miss the trivial case.

## Similar
[[Minimum Cost to Cut a Stick]] · [[Scramble String]]
