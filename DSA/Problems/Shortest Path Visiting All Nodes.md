---
pattern: dp-bitmask
family: dp
difficulty: hard
leetcode: 847
time: O(n·2^n)
space: O(n·2^n)
tags:
  - dp
---
# Shortest Path Visiting All Nodes
[LC 847](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) · [[Bitmask DP]]

## Recognize
Visit all nodes in a small undirected graph (`n ≤ 12`), shortest path, revisits allowed. "Visit all + shortest" + small n → BFS + bitmask (BFS-based TSP).

## Insight
State `(node, mask)` where `mask` is which nodes you've collected. Total states = `12 × 4096 ≈ 50K`. BFS from every node simultaneously (all are valid starts).

## Approach
1. Initialize queue with `(i, 1 << i)` for every node `i`, all at distance 0.
2. BFS: from `(u, mask)`, push every neighbor `v` with state `(v, mask | (1 << v))`.
3. Stop on the first state with `mask == (1 << n) − 1`.

## Pitfalls
- Revisits allowed — `mask` is *collection*, not *movement restriction*.
- Use a separate `visited[node][mask]` — don't reuse distance as both "computed" and "unvisited" sentinel.
- Check visited **before pushing** to the queue, not after popping.

## Similar
[[Find the Shortest Superstring]] · [[Smallest Sufficient Team]]
