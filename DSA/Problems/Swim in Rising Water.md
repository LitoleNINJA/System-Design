---
pattern: graph-shortest-path
family: graph
difficulty: hard
leetcode: 778
time: O(n²·log n)
space: O(n²)
tags:
  - graph
---
# Swim in Rising Water
[LC 778](https://leetcode.com/problems/swim-in-rising-water/) · [[Graph - Shortest Path]]

## Recognize
Grid where path cost is the **max cell value along the path** (not the sum). Min over all paths of this max. Two approaches: binary search the answer, or Dijkstra with `max` instead of `+`.

## Insight
You only care about the distance to **one target**, not all nodes. Skip the `dist[]` array — the priority queue itself carries the state, ordered by `max_along_path`.

## Approach
**Dijkstra variant:**
`priority_queue<(max_so_far, r, c)>` min-heap. Pop, if target, return. Else push each unvisited neighbor with `max(max_so_far, grid[nr][nc])`.

**Or binary search the answer:**
Binary search `t ∈ [0, max(grid)]`. For each `t`, BFS using only cells with value ≤ t. Find smallest `t` where target is reachable.

## Pitfalls
- The recurrence uses `max`, not `+` — this isn't a standard sum-of-weights shortest path.
- For target-only Dijkstra, mark visited on pop (when finalized), not on push.
- Binary search bounds: low = `grid[0][0]`, high = `max(grid)`.

## Similar
[[Minimum Obstacle Removal to Reach Corner of Grid]] · [[Number of Ways to Arrive at Destination]]
