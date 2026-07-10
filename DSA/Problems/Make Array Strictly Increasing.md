---
pattern: dp-binsearch
family: dp
difficulty: hard
leetcode: 1187
time: O(n·m log m)
space: O(n·m)
tags:
  - dp
---
# Make Array Strictly Increasing
[LC 1187](https://leetcode.com/problems/make-array-strictly-increasing/) · [[DP - Overview]]

## Recognize
Min swaps to make `arr1` strictly increasing, replacing any element with any element from `arr2`.

## Insight
State = `(index, last_value)`. `last_value` can be huge → use `map<int, int>` per index. Sort + dedupe `arr2` first so binary search is clean.

## Approach
`dp(i, last_val)` — min swaps for `arr1[i..n−1]` given previous kept value was `last_val`.
- **Keep:** if `arr1[i] > last_val`, recurse `dp(i+1, arr1[i])`, no extra cost.
- **Swap:** binary-search (`upper_bound`) in sorted `arr2` for smallest value `> last_val`. Recurse `dp(i+1, that_value) + 1`.

Initial call: `dp(0, −1)` (sentinel so first element can always be kept).

## Pitfalls
- Sort and dedupe `arr2` first — duplicates are useless.
- `upper_bound` bound check: `pos < arr2.size()`, **not** `< arr1.size()`.
- Memo with `map<int, int>` per index — `last_val` is too large/sparse for an array.
- Return `INF` from infeasible branches; final answer −> if `INF`, output `−1`.

## Similar
[[Russian Doll Envelopes]] · [[Maximum Number of Events That Can Be Attended II]]
