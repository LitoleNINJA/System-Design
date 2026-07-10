---
pattern: dp-dag
family: dp
difficulty: hard
leetcode: 1857
time: O(n·c + e)
space: O(n·c)
tags:
  - dp
  - graph
---
# Largest Color Value in a Directed Graph
[LC 1857](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) · [[DP - Overview]]

## Recognize
DAG, each node has a color (26 max). Find the max count of any single color along any path.

## Insight
Topological order = "all predecessors processed before successors" → propagate color counts forward along this order. State = `dp[node][color]`.

## Approach
1. Build graph + indegree array.
2. Topological sort via Kahn's BFS.
3. For each `u` in topo order, propagate `dp[u][c]` to every successor `v`: `dp[v][c] = max(dp[v][c], dp[u][c])`. Then bump `dp[u][color_of_u] += 1` for `u`'s own contribution.

Cycle detection: if `processed_count != n`, there's a cycle → return `−1`.

## Pitfalls
- Bump `dp[u][color_of_u]` **after** propagation; otherwise you double-count.
- Cycle check is mandatory — the problem allows cyclic input.
- 26 × n entries; iterating over all 26 colors per node-pair is fine.

## Similar
[[Number of Ways to Arrive at Destination]]
