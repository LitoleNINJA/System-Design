---
pattern: graph-traversal
family: graph
difficulty: hard
leetcode: 2290
time: O(n·m)
space: O(n·m)
tags:
  - graph
---
# Minimum Obstacle Removal to Reach Corner of Grid
[LC 2290](https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/) · [[Graph - BFS DFS]]

## Recognize
Grid with cells {0, 1}. Find path from top-left to bottom-right minimizing **sum of 1s** crossed. Edge weights are 0 or 1 only → **0/1 BFS**.

## Insight
Dijkstra works but O((V+E) log V) is overkill when weights are binary. A **deque** lets you process all 0-weight neighbors before 1-weight ones, achieving O(V+E).

## Approach
`deque<(r, c, cost)>`. Push `(0, 0, 0)`.
```
while not empty:
    (r, c, d) = pop_front
    if (r, c) == target: return d
    if (r, c) in visited: continue
    mark visited
    for each neighbor (nr, nc):
        if grid[nr][nc] == 0:
            push_front (nr, nc, d)        // free
        else:
            push_back (nr, nc, d + 1)     // costs 1
```

## Pitfalls
- Use a **deque**, not a queue or priority queue.
- **0-cost edges go to the front**, 1-cost to the back — this maintains BFS's monotone-distance invariant.
- Check visited after pop, not push — duplicates in the deque are fine, they're cheap to skip.

## Similar
[[Swim in Rising Water]]
