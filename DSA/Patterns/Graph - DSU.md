---
type: pattern
name: Graph - DSU
family: graph
tags: [pattern, graph]
---
# Graph — DSU (Union-Find)

## Signature
Disjoint-set data structure for connectivity queries: `find` returns the component root; `union` merges two components. With path compression + union-by-size/rank, both operations are near-O(1) amortized (α(n)). Use when the problem is connectivity-shaped *and* the operations are either additions only, or can be reordered to be additions only.

## Recognize
- "Are u and v connected?" / "How many components?" / "Component size of u?"
- **Kruskal MST** — process edges in weight order, union endpoints if not yet connected
- **Offline query processing** — many independent queries, each with a threshold; sort + sweep
- "Adding edges over time" / "merging entities" — DSU shines on additions, **not** deletions

## Canonical state / structure
- `par[v]` — parent pointer; `par[v] = v` iff v is a root
- `size[v]` (or `rank[v]`) — meaningful only at the root, for union-by-size/rank
- `find(v)` with path compression: `par[v] = find(par[v])`
- `union(a, b)`: find roots; if different, smaller root → larger root, accumulate size

## Transition / steps
- **Make-set:** `par[i] = i; size[i] = 1;`
- **Find with compression:** `find(v) = (par[v] == v) ? v : (par[v] = find(par[v]));`
- **Union by size:** find both roots; if equal, return; if `size[a] < size[b]` swap; then `par[b] = a; size[a] += size[b];`

## Variants
- **Kruskal's MST** — sort edges ascending; union endpoints if disjoint; accumulate weight.
- **Offline threshold sweep** — sort queries by threshold, sort edges/items in matching order, sweep both with a forward-only pointer. (Checking Existence of Edge Length Limited Paths)
- **Value-ordered activation on a tree/graph** — process nodes in value order, union with already-activated neighbors; count paths via `k(k+1)/2` per component. For "path bounded by max/min value." (Number of Good Paths)
- **DSU with rollback** — when undoing unions is needed (divide-and-conquer over time), store union deltas on a stack. No path compression then (it would break rollback).
- **Weighted / potential DSU** — extra per-node potential so `find` returns offset relative to root; supports "is u exactly k apart from v" style.
- **Small-to-large merging on trees** — process tree problems by merging children's data into the parent, always merging the smaller side into the larger.
- **Counting components** — decrement a counter on each successful union.

## Pitfalls
- After `swap(a, b)` to make `a` the larger root, write `par[b] = a`. Assigning `par[a] = b` still produces correct connectivity but breaks union-by-size balancing (root's `size` becomes stale).
- Without path compression OR union-by-size, find degrades to O(n) per call worst case.
- `size[v]` is meaningful only at the root — don't read non-root nodes' `size[]`.
- Offline-style problems: do **not** lose original query indices when sorting. Pair `(query, idx)` before sort, or sort an index array.
- DSU does **not** support deletion. If you need it, reach for link-cut trees or rollback DSU with offline structure.
- Strict `<` vs `≤` thresholds in offline sweeps — re-read the problem statement.
- `map<vector<int>, int>` as a "query → index" lookup silently collapses duplicate queries. Always pair the index alongside, never key on the query payload.

## Problems
- [[Checking Existence of Edge Length Limited Paths]]
- [[Number of Good Paths]]
