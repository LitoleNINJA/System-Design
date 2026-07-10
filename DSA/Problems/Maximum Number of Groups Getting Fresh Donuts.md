---
pattern: dp-bitmask
family: dp
difficulty: hard
leetcode: 1815
time: O(states · b)
space: O(states)
tags:
  - dp
---
# Maximum Number of Groups Getting Fresh Donuts
[LC 1815](https://leetcode.com/problems/maximum-number-of-groups-getting-fresh-donuts/) · [[Bitmask DP]]

## Recognize
- "Pick an order over n items to maximize something" — combinatorial, `n` small-ish but not tiny (n ≤ 30)
- Original values don't matter — only some property does (here: `% b`)
- Few distinct property classes (≤ 8) — many items collapse to identical equivalence classes

## Insight
Bitmask over groups dies at `2^30`. But identical-remainder groups are interchangeable for the future, so the state collapses from "which subset of indices remain" to "how many of each remainder remain" — a tuple of ≤ 8 counts. `rem` is a function of the count-tuple (given the initial counts), so it doesn't need to be a separate state dimension. Mod-0 groups are always happy and don't shift leftover — pull them out as a constant `+freq[0]`.

## Approach
1. Bucket: `freq[r]` = count of groups with size `% b == r`. Save `freq[0]` as a bonus.
2. State = current `freq` tuple over r = 1..b-1. Memo: `unordered_map<array<int,9>, int, customHash>`.
3. Compute `rem` on the fly: `rem = (Σ_r r · (initial_freq[r] − cur_freq[r])) mod b`.
4. Transition: for each `r` with `cur_freq[r] > 0`, place one group of class `r` next; it's happy iff `rem == 0`. Recurse on `cur_freq − e_r`. Take max.
5. Answer = `recurse(initial_freq) + freq[0]`.

## Pitfalls
- `array<int, 9>` (not 8) — `batchSize` up to 9 means remainders 0..8; you need 9 slots even if the choice loop is 1..8.
- `unordered_map<array, int>` won't compile without a custom hash — `std::array` has no default `std::hash`. Either provide one or fall back to `map` (operator< works).
- `rem` is **weighted** by the remainder: `i * (freq[i] - fre[i])`, not just the count of placed groups.
- Mod-0 groups: add `+freq[0]` to the final answer; exclude them from the choice loop (they're free, don't affect leftover).
- Restore `fre[i]++` after the recursive call when iterating choices — otherwise the loop explores cumulative removals.
- `recurse` must `return dp[fre] = ans;` at the end. Falling off the function is UB and you'll spend an hour wondering why memo hits return garbage.
- Optional greedy: if some `r` makes `(rem + r) % b == 0`, taking it next is always at least as good (the *next* group becomes happy). Cuts work; not required for correctness.
