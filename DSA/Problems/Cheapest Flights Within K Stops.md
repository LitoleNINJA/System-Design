---
pattern: graph-traversal
family: graph
difficulty: medium
leetcode: 787
time: O(K·E)
space: O(V)
tags:
  - graph
---
# Cheapest Flights Within K Stops
[LC 787](https://leetcode.com/problems/cheapest-flights-within-k-stops/) · [[Graph - BFS DFS]] · [[Graph - Shortest Path]]

## Recognize
Shortest-cost path with **≤ K stops** constraint. Two competing priorities — cost (weight) and depth (level).

## Insight
Dijkstra prioritizes cost over depth — it can take a cheap detour that exceeds K stops. Use **level-by-level BFS** instead: at each BFS layer, relax all reachable nodes. After K+1 layers, stop.

Equivalently: Bellman-Ford with at most K+1 iterations.

## Approach
BFS with a plain queue, `(node, cost)`. Loop K+1 times:
```
for level in 0..K:
    new_dist = copy(dist)        // snapshot — relax from previous level only
    for each (u, v, w):
        new_dist[v] = min(new_dist[v], dist[u] + w)
    dist = new_dist
return dist[dst]
```

## Pitfalls
- **Don't** use a priority queue — order by cost ignores the K-stop limit.
- Bellman-Ford style: snapshot `dist[]` per level, else one level's relaxation cascades into the next.
- "Stops" vs "edges": K stops = K+1 flights = K+1 relaxations.

## Similar
[[Number of Ways to Arrive at Destination]] · [[Swim in Rising Water]]
