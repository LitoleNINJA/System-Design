---
pattern: graph-shortest-path
family: graph
difficulty: medium
leetcode: 1976
time: O((V+E) log V)
space: O(V)
tags:
  - graph
---
# Number of Ways to Arrive at Destination
[LC 1976](https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/) · [[Graph - Shortest Path]]

## Recognize
Count the number of distinct shortest paths from 0 to n-1. Standard Dijkstra plus a parallel `ways[]` count.

## Insight
While relaxing in Dijkstra:
- New shortest distance → reset `ways[v] = ways[u]`.
- Same shortest distance (tie) → accumulate `ways[v] += ways[u]`.

## Approach
Standard Dijkstra, with one extra array.

```
dist[0] = 0; ways[0] = 1
pq.push((0, 0))
while pq:
    (d, u) = pop_min
    if d > dist[u]: continue
    for (v, w) in adj[u]:
        if d + w < dist[v]:
            dist[v] = d + w
            ways[v] = ways[u]
            pq.push((dist[v], v))
        elif d + w == dist[v]:
            ways[v] = (ways[v] + ways[u]) % MOD
return ways[n-1]
```

## Pitfalls
- The "ties" branch doesn't push to PQ — `v` is already going to be popped at this distance.
- Modulo every accumulation (n can be up to 200, but counts can blow up).
- Don't reset `ways[v]` on the equal-distance branch — only on strict improvement.

## Similar
[[Swim in Rising Water]] · [[Cheapest Flights Within K Stops]]
