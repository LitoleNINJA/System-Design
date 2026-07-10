---
pattern: dp-partition
family: dp
difficulty: hard
leetcode: 1335
time: O(n²·d)
space: O(n·d)
tags:
  - dp
---
# Minimum Difficulty of a Job Schedule
[LC 1335](https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/) · [[Partition DP]]

## Recognize
Partition `jobs[0..n-1]` into exactly `d` contiguous segments. Cost of a segment = its max. Minimize the sum.

## Insight
Per-segment cost (max) is monotone as you extend rightward — track it incrementally inside the partition-endpoint loop instead of recomputing.

## Approach
`dp(i, d)` — min total difficulty for `jobs[i..n-1]` partitioned into `d` segments.
Sweep `j` from `i` to `n−d`, tracking `curMax = max(curMax, jobs[j])`:
```
dp(i, d) = min over j of: dp(j+1, d−1) + curMax
```

## Pitfalls
- `j` must stop at `n−d` — leave room for remaining `d−1` segments.
- **Base:** `d == 1` → `max(jobs[i..n-1])`, not `0`.
- Infeasible: `n < d` → `−1`.
- Track `curMax` **inside** the `j` loop to keep it O(n²·d).

## Similar
[[Palindrome Partitioning III]] · [[Partition Array for Maximum Sum]]
