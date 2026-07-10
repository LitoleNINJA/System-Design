---
type: pattern
name: Permutation DP
family: dp
tags: [pattern, dp]
---
# Permutation DP

## Signature
Count or evaluate permutations under ordering constraints.

## Recognize
- "Number of permutations such that ..."
- Each element placed exactly once
- Constraint on relative position (visible-from-left, blocked-by, etc.)

## Canonical state / structure
`dp(i, k)` — for `i` elements placed so far, `k` is some structural count (visible, inversions, ...).

## Transition / steps
Pick the **extreme element that interacts least with others** — usually the smallest or the largest. It gives the cleanest subproblem.

E.g. K Sticks Visible — decide for the **shortest** stick:
- Place at start (visible): `dp(i−1, k−1)`
- Place anywhere else (hidden): `dp(i−1, k) · (i−1)`

## Variants
- **Inversions via insertion** — place new element into a permutation of size `i−1`; positions from the end create 0..i−1 new inversions. (K Inverse Pairs)
- **Compressed structure** — only the *count* matters, not which specific elements. State stays low-dim.

## Pitfalls
- Picking the wrong extreme element doubles the case count. Choose the one whose placement doesn't entangle with future decisions.
- Multiply, don't add, when many placements yield equivalent subproblems.
- `dp[0][0] = 1` (empty permutation has one arrangement), not `0`.

## Problems
- [[Number of Ways to Rearrange Sticks With K Sticks Visible]]
- [[K Inverse Pairs Array]]
