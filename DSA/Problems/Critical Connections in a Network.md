---
pattern: graph-traversal
family: graph
difficulty: hard
leetcode: 1192
time: O(V + E)
space: O(V)
tags:
  - graph
---
# Critical Connections in a Network
[LC 1192](https://leetcode.com/problems/critical-connections-in-a-network/) · [[Graph - BFS DFS]]

## Recognize
Find all **bridges** in an undirected graph — edges whose removal disconnects the graph. Tarjan's algorithm.

## Insight
DFS-tree intuition: an edge `(u, v)` is a bridge iff `v` (and its subtree) has no back-edge bypassing `(u, v)`. Track entry time `tin[v]` and the earliest reachable ancestor `low[v]`. Bridge condition: `low[to] > tin[v]`.

## Approach
Arrays: `visited[]`, `tin[]`, `low[]`, global `timer`.

```
def dfs(v, parent):
    visited[v] = true
    tin[v] = low[v] = timer++
    for to in adj[v]:
        if to == parent: continue            // skip the edge we came on
        if visited[to]:
            low[v] = min(low[v], tin[to])    // back edge
        else:
            dfs(to, v)
            low[v] = min(low[v], low[to])    // tree edge
            if low[to] > tin[v]:
                bridges.add((v, to))
```

## Pitfalls
- "Skip parent" is necessary for undirected graphs — without it, the immediate back-edge to parent falsely lowers `low[v]`.
- Parallel edges (two distinct edges between `u` and `v`) need special handling — skip by *edge id*, not by node id.
- `tin` and `low` must use the same timer increment.

## Similar
[[Minimum Edge Toggles on a Tree]]
