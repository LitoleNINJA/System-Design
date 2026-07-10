---
pattern: graph-topo
family: graph
difficulty: hard
leetcode: 2127
time: O(n)
space: O(n)
tags:
  - graph
---
# Maximum Employees to Be Invited to a Meeting
[LC 2127](https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/) · [[Graph - Topo]]

## Recognize
- Each node has exactly **one outgoing edge** (`favorite[i]`) → functional graph
- "Round table, each next to their favorite" — geometric constraint that splits the problem into two structural cases

## Insight
A functional graph is a forest of trees rooted at cycles. Two contributors to the seating:
- **Cycle of length ≥ 3:** take the whole cycle; nobody else fits.
- **Cycle of length 2 (mutual pair):** seat both, then chain people on each side — every link's "favorite" still sits adjacent to them along the chain. **All 2-cycles sum together** (independent contributions).

Answer = `max(longest_long_cycle, sum_over_2cycles(2 + chain_a + chain_b))`.

## Approach
1. Compute `indeg[v]` from `favorite[]`.
2. **Kahn's peel** seeded with in-deg-0 nodes. As you peel `u → v = favorite[u]`, propagate `depth[v] = max(depth[v], depth[u] + 1)`. After Kahn, nodes with `indeg > 0` are exactly the cycle nodes; `depth[v]` for a cycle node is the longest external chain ending at it.
3. **Walk each cycle** (start from any unvisited cycle node, follow `favorite[]` until you return). Mark visited.
   - `len ≥ 3` → candidate for `longest_long_cycle`.
   - `len == 2` → add `2 + depth[a] + depth[b]` to `sum_of_2cycles`.
4. Return `max(longest_long_cycle, sum_of_2cycles)`.

## Pitfalls
- Direction matters: chains flow **into** the cycle. Depth is "longest chain ending at a cycle node," computed via Kahn's depth propagation — don't reverse the graph and DFS.
- All 2-cycles contribute **additively**. Easy bug: max-ing across 2-cycles instead of summing them.
- A pure 2-cycle with no chains has contribution `2`, not `0`. Init `depth` to 0 and the formula works out.
- 2-cycle is **not** a long cycle — case-split on `cycle_len == 2` vs `> 2`. Don't conflate.
- The cycle walk must mark visited so the same cycle isn't counted twice when starting from different remaining nodes.
- `n` up to 1e5 — keep it strictly linear; no O(n²) cycle-finding.
