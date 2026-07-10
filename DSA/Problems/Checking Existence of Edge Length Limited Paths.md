---
pattern: graph-dsu
family: graph
difficulty: hard
leetcode: 1697
time: O((E + Q) log(E + Q))
space: O(N + Q)
tags:
  - graph
---
# Checking Existence of Edge Length Limited Paths
[LC 1697](https://leetcode.com/problems/checking-existence-of-edge-length-limited-paths/) · [[Graph - DSU]]

## Recognize
- Many independent connectivity queries with a per-query weight constraint
- Naive "BFS/DFS per query with edge filter" → O(Q · (V+E)) dies at 10^5 × 10^5
- Queries can be **reordered** without changing answers → strong signal for offline processing

## Insight
Start with empty graph, add edges in weight order, answer queries in limit order. Adding edges to a connectivity structure (DSU) is near-O(1) amortized; removing edges would need link-cut trees. So **sort both ways ascending** and sweep — at the moment we process query `(p, q, L)`, every edge with `w < L` is already unioned.

## Approach
1. Sort edges ascending by weight.
2. Build a queries-with-original-index list `(p, q, limit, idx)`; sort ascending by `limit`.
3. Sweep queries; maintain an edge pointer `j` that only moves forward. For each query, union all edges with `w < limit`. Then check `find(p) == find(q)` and write to `ans[idx]`.
4. DSU with union-by-size + path compression.

## Pitfalls
- **Don't round-trip query → index through `map<vector<int>, int>`** — if two queries are identical, the map collides and only the last index survives → silent WA on duplicates. Attach the original index to each query as a 4th field before sorting.
- Strict `<` vs `≤`: the problem says **strictly less than limit**. Off-by-one if you use `<=`.
- Edge pointer is monotonic — never restart it per query, or you blow up to O(Q · E).
- Union-by-size: after `swap(a, b)` ensures `a` is the larger root, parent assignment must be `par[b] = a` (smaller → larger). Reversed still gives correct connectivity but breaks the balancing optimization.
- Sorting `queries` in place loses the original index — pair it explicitly first.
