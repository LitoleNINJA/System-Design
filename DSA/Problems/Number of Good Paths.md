---
pattern: graph-dsu
family: graph
difficulty: hard
leetcode: 2421
time: O((n + e) α(n) + n log n)
space: O(n)
tags: [dsa, graph]
---
# Number of Good Paths
[LC 2421](https://leetcode.com/problems/number-of-good-paths/) · [[Graph - DSU]]

## Recognize
- Tree + node values, count paths where endpoints equal the **max** on the path
- "Path constrained by max/min value along it" → activate nodes in value order, union as you go
- Pairs counting inside connected components → DSU + `k(k+1)/2`

## Insight
Endpoints equal V and interior ≤ V means V is the max on the path. Process values smallest → largest. When processing value `val`, fuse each `val`-node with already-activated neighbors (`a[neighbor] ≤ val`). Then any component holding `k` of this batch's nodes contributes `k*(k+1)/2` good paths whose max is exactly `val` — `k` singletons + `k(k-1)/2` pairs.

## Approach
1. DSU over all n nodes (everyone "exists"; the `≤ val` guard gates fusion).
2. Group node indices by value in a sorted `map<int, vector<int>>`.
3. For each value `val` ascending:
   - For each node `u` in the batch, for each neighbor `w`: if `a[w] <= val`, `union(u, w)`.
   - Group the batch's nodes by `find(u)`; for each root with count `k`, `ans += k*(k+1)/2`.

## Pitfalls
- **Variable shadowing**: don't name the neighbor `v` when `v` is already the current value — `a[v] <= v` becomes "value of neighbor ≤ neighbor index," silent garbage. Name them distinctly (`val` / `w`).
- The `k*(k+1)/2` formula folds singletons (`k=1 → 1`) and pairs together — don't add singletons separately.
- Counting per-union (`+1` each union) is wrong: a component of k same-value nodes has `k(k+1)/2` paths, not `k-1`. Count per component after all unions in the batch.
- Use `long long` for the accumulator — answer reaches ~`C(3e4, 2)` ≈ 4.5e8.
- Don't name a local `map` — shadows `std::map`.
- DSU activation: every node is in the DSU from the start; the `a[w] <= val` check (not lazy insertion) is what enforces "only fuse already-reachable-at-this-level" nodes.
