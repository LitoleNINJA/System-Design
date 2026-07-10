---
type: pattern
name: Graph - Shortest Path
family: graph
tags: [pattern, graph]
---
# Graph — Shortest Path

## Signature
Find the minimum-weight path from a source (or all-pairs) in a weighted graph. Choose the algorithm by edge-weight structure.

## Recognize
| Edges | Algorithm | Complexity |
|---|---|---|
| Unweighted | BFS | O(V+E) |
| Weights ∈ {0, 1} | 0/1 BFS (deque) | O(V+E) |
| Non-negative weights | **Dijkstra** | O((V+E) log V) |
| Negative weights, no neg cycle | Bellman-Ford | O(V·E) |
| Dense, all-pairs | Floyd-Warshall | O(V³) |
| DAG | Topo-sort + relax | O(V+E) |

## Canonical state / structure
Dijkstra: `priority_queue<pair<dist, node>>` (min-heap) + `dist[]`. Pop smallest, relax neighbors, skip stale entries.

## Transition / steps
```
dist[src] = 0
push (0, src)
while queue:
    (d, u) = pop_min
    if d > dist[u]: continue   // stale
    for (v, w) in adj[u]:
        if d + w < dist[v]:
            dist[v] = d + w
            push (dist[v], v)
```

## Variants
- **Target-only Dijkstra** — when you only need distance to a single target, no `dist[]` needed; the priority queue itself carries the state. (Swim in Rising Water)
- **Count shortest paths** — parallel `ways[]` array. On strict improvement: `ways[v] = ways[u]`. On equality: `ways[v] += ways[u]`. (Number of Ways to Arrive at Destination)
- **Constrained Dijkstra** — extra state in the priority queue (k stops left, current color, etc.). State becomes `(dist, node, extra)`.
- **K-th shortest path** — modified Dijkstra without "visited", allow re-pops.
- **Non-uniform edge cost from a gate** — entry time = `max(arrival, gate)`; when waiting is allowed (oscillate to burn even time) plus a parity constraint, bump the candidate by 1 on parity mismatch. (Minimum Time to Visit a Cell In a Grid)

## Pitfalls
- Dijkstra fails on **negative edges** — use Bellman-Ford or SPFA instead.
- "Skip stale" check is essential — without it, every entry in the PQ gets processed, O((V+E)²).
- `dist[]` must use a large sentinel (e.g. `LLONG_MAX/2`) — adding to `LLONG_MAX` overflows.
- For unweighted graphs Dijkstra is overkill — BFS is faster and simpler.

## Problems
- [[Swim in Rising Water]]
- [[Number of Ways to Arrive at Destination]]
- [[Cheapest Flights Within K Stops]]
- [[Minimum Time to Visit a Cell In a Grid]]
