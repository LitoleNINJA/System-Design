---
pattern: dp-sequence
family: dp
difficulty: hard
leetcode: 354
time: O(n log n)
space: O(n)
tags:
  - dp
---
# Russian Doll Envelopes
[LC 354](https://leetcode.com/problems/russian-doll-envelopes/) · [[DP - Overview]]

## Recognize
Strictly nest envelopes by both width and height. Reduce 2D LIS to 1D LIS via sorting.

## Insight
Sort by **width ascending, height descending for ties**. Then run LIS on heights. Descending height for same width prevents picking two same-width envelopes (they can't both nest).

## Approach
1. Sort: `(w ↑, h ↓)` on ties.
2. LIS on heights — O(n log n) with `lower_bound` for strict increase.

## Pitfalls
- **Strictly** increasing → `lower_bound` (replace ≥), not `upper_bound`.
- Same-width tie-break must be **descending** height — ascending would allow same-width envelopes to chain incorrectly.
- LIS classic mistake: confusing array `d` with the actual increasing subsequence (it isn't).

## Similar
[[Arithmetic Slices II - Subsequence]]
