---
pattern: heap
family: array
difficulty: hard
leetcode: 2386
time: O(n log n + k² log k)
space: O(n + k²)
tags: [dsa, array]
---
# Find the K-Sum of an Array
[LC 2386](https://leetcode.com/problems/find-the-k-sum-of-an-array/) · [[Heap]]

## Recognize
- Huge n (1e5) paired with tiny k (≤ 2000) — the loudest line in the statement. Kills enumeration; demands building the top-k incrementally.
- "k-th largest/smallest over an exponential space" (here: all 2^n subsequence sums).

## Insight
Largest sum is just all the positive numbers. Leaving out a positive or adding a negative cost the same kind of thing — we only care about the abs val, subtracted from max sum. So make all the -ves +ve, put everything in one pile: **k-th largest sum = maxSum − (k-th smallest subset sum of the pile)**.

## Approach
Sort pile ascending. State = `(sum, i)` — subset sum + its own frontier (last index used). Build discipline: subsets assemble left-to-right only, so each subset is constructed exactly once and no element repeats.

- Min-heap seeded with singletons `(a[i], i)`.
- Pop `(sum, i)`, push `(sum + a[j], j)` for `j` in `(i, i+K)` — cap fan-out at K: the (m+1)-th child has m smaller siblings ahead of it, unreachable within k pops.
- Rank 1 is cost 0 (remove nothing) and lives *outside* the heap: `if (k == 1) return sum;` else pop k−2 times, answer = `sum − pq.top()`.

Leaner variant (not needed to pass): exactly 2 children per pop — extend `(sum + a[i+1], i+1)`, replace `(sum − a[i] + a[i+1], i+1)` → O(k log k), heap stays O(k).

## Pitfalls
- Bare sums as states → illegal reuse and duplicates (pile `[1,2,100]`: pop `{1,2}` = 3, "add back 1" → 4, not a real subset sum). A global removed/can_take list can't fix this — legality is per-state; each state carries its own frontier.
- The rank-outside-the-heap off-by-one: heap top is rank 2 before any pops → pop k−2, not k−1; k = 1 never touches the heap.
- Folding that edge case into loop algebra (`k--` + `while(--k)`): k = 1 → 0 → −1 is truthy → ~2³¹ spins, TLE on `[-1,1], k=1`. Guard the special rank in plain sight.
- maxSum up to 1e14 and pushed costs past 1e9 → `long long` for all sums.
