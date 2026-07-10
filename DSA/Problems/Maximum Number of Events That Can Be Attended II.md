---
pattern: dp-binsearch
family: dp
difficulty: hard
leetcode: 1751
time: O(n·k log n)
space: O(n·k)
tags:
  - dp
---
# Maximum Number of Events That Can Be Attended II
[LC 1751](https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/) · [[Knapsack DP]]

## Recognize
Pick up to `k` non-overlapping events to maximize value. Like Job Scheduling but with a pick limit.

## Insight
Sort events by start time. After taking event `i`, the next pickable event starts strictly after `end[i]` — binary-search for it.

## Approach
Sort by start time. Precompute `startTimes[]`.
`dp(i, k)` — max value from events `[i..n−1]` with `k` picks remaining.
- **Skip:** `dp(i+1, k)`
- **Take:** `dp(pos, k−1) + value[i]` where `pos = upper_bound(startTimes, end[i])` — first event whose start time is **after** `end[i]`.

Base: `i == n` or `k == 0` → `0`. Answer: `dp(0, k)`.

## Pitfalls
- Final answer is `dp(0, k)`, **not** `dp(0, 0)` (which is "0 picks remaining → 0").
- Sort by start, but binary-search uses `upper_bound` on `start > end` — events whose start is strictly past current end.
- O(n·k log n) — without binary search, falls to O(n²·k) and may TLE.

## Similar
[[Make Array Strictly Increasing]]
