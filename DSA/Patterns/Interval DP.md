---
type: pattern
name: Interval DP
family: dp
tags: [pattern, dp]
---
# Interval DP

## Signature
Cost of merging or splitting a range. `dp(i, j)` over substring/subarray, built from smaller intervals.

## Recognize
- Range `[i, j]` is the natural unit of work
- Merging adjacent intervals or splitting at a point `k`
- Often "burst", "remove", "cut", "merge"

## Canonical state / structure
`dp(i, j)` — best value for the interval `[i, j]`.

## Transition / steps
- **Split:** `dp(i, j) = min/max over k of: dp(i, k) + dp(k, j) + merge_cost`
- **Iterate by length** (bottom-up): outer loop `len = 2..n`, inner over starts `i`.

## Variants
- **Extra state from outside** — when boxes/elements outside `[i, j]` affect decisions inside, add a dim. (Remove Boxes)
- **Coordinate as index** — when cuts/cards are sparse over a huge axis, indices into the sorted positions array become the state. (Min Cost to Cut a Stick)
- **Two intervals** — on two strings or two arrays, e.g. `dp(i1, i2, len)`. (Scramble String)

## Pitfalls
- Split bound must be strict (`i < k < j`) — endpoint inclusion causes infinite recursion.
- Length must increase monotonically in bottom-up — outer loop is `len`, not `i`.
- Don't forget the "leaf" interval (length 1) as the base.

## Problems
- [[Remove Boxes]]
- [[Minimum Cost to Cut a Stick]]
- [[Scramble String]]
- [[Palindrome Partitioning III]]
