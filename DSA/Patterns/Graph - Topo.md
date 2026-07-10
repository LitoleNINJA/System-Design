---
type: pattern
name: Graph - Topo
family: graph
tags: [pattern, graph]
---
# Graph — Topological Sort

## Signature
Order the nodes of a DAG (or peel nodes off a graph) using in-degree. Use for scheduling with prerequisites, longest-path-in-DAG, "process when ready" elimination, and extracting cycles by peeling everything else.

## Recognize
- "Prerequisites" / "dependencies" → Kahn's BFS in topological order
- "Longest path / earliest finish time in a DAG" → relax along topo order
- "Each node has out-degree 1" → functional graph; cycles are what remains after peeling in-deg-0 nodes
- "Detect cycle / order possible?" → if Kahn processes < n nodes, there's a cycle

## Canonical state / structure
- `indeg[v]` per node
- Queue seeded with all in-deg-0 nodes
- Optional `depth[v]` / `dp[v]` / `time[v]` propagated along topo edges as you peel

## Transition / steps
- **Kahn's BFS** — push all in-deg-0 nodes. Pop `u`; for each `(u → v)`: decrement `indeg[v]`; if 0, push.
- **DAG longest path / scheduling** — same as Kahn, but propagate `dp[v] = max(dp[v], dp[u] + w(u, v))` as you peel.
- **Functional graph cycle extraction** — Kahn peel; survivors (`indeg > 0`) are the cycle nodes. Walk each cycle from any survivor.
- **Cycle detection (any graph)** — if Kahn order has fewer than n entries, the unvisited remainder contains a cycle.

## Variants
- **Functional graph (out-deg 1)** — peel in-deg-0, what's left is cycles; chains attach to cycle nodes. Per-cycle-node depth = longest external chain. (Maximum Employees to Be Invited to a Meeting)
- **Lex-smallest topo order** — swap queue for priority queue.
- **DAG with weighted edges** — propagate DP along edges as you peel.
- **Two-pass DAG DP** — once forward (topo), once reverse (reverse topo) — for problems like "longest path through node v."

## Pitfalls
- Edge direction: in dependency problems, edge `a → b` typically means "a must precede b." State this explicitly before coding.
- For functional graphs, "cycle membership" is "indeg > 0 after peeling" — simpler than DFS back-edge detection.
- When DP-propagating along topo edges, initialize per-node values *before* peeling; don't expect Kahn to revisit.
- Forgetting that *all* 2-cycles in a functional graph sum together — they don't compete with each other.
- Kahn produces *one* valid topological order, not the unique one. If you need a specific order, sort or use a priority queue.

## Problems
- [[Maximum Employees to Be Invited to a Meeting]]
