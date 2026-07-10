---
type: pattern
name: Graph - BFS DFS
family: graph
tags: [pattern, graph]
---
# Graph — BFS / DFS

## Signature
Traverse an unweighted or uniformly-weighted graph. Use BFS for shortest-path-by-edges; DFS for structural questions (components, bridges, articulation, cycles).

## Recognize
- "Shortest path" + **unweighted** edges → BFS (O(V+E))
- "Connected components" → BFS or DFS
- "Least number of moves" → BFS
- "Bridges / articulation points / SCCs" → DFS with timers (Tarjan)
- "Edge weights 0/1 only" → **0/1 BFS** with a deque

## Canonical state / structure
- BFS: `queue<(node, ...)>` + `visited[]`
- DFS: recursion or stack + `visited[]`; for bridges add `tin[]`, `low[]`, global `timer`.

## Transition / steps
- **BFS** — pop, mark visited if not yet, push neighbors not visited. Level distance = parent + 1.
- **0/1 BFS** — `deque`. Edge weight 0 → push front; edge weight 1 → push back. Pop from front. Guarantees uniform distance growth.
- **Bridge finding (Tarjan)** — DFS, set `tin[v] = low[v] = timer++` on entry. For tree edge to `to`: recurse, then `low[v] = min(low[v], low[to])`. For back edge to `p` (not parent): `low[v] = min(low[v], tin[p])`. Edge `(v, to)` is a bridge iff `low[to] > tin[v]`.

## Variants
- **BFS for fewest stops with edge cost** — use plain queue (level-by-level), not priority queue. Dijkstra would prioritize cost over level. (Cheapest Flights Within K Stops)
- **0/1 BFS** — deque trick for binary-weighted edges. (Min Obstacle Removal)
- **Leaf-inward DFS / tree peeling** — when each leaf has no choice, decide and remove. (Min Edge Toggles on a Tree)
- **Bridges (Tarjan)** — `tin` / `low` / timer arrays. (Critical Connections)
- **Bidirectional BFS** — meet in the middle when start and target are both fixed; halves the explored frontier.

## Pitfalls
- Mark visited **when pushing**, not when popping — duplicates otherwise.
- For undirected graphs, skip the immediate parent to avoid trivial back-edge confusion in Tarjan.
- "Shortest path in unweighted" — never reach for Dijkstra; it's strictly slower than BFS.
- For 0/1 BFS, **don't** mark visited on push for the deque-front case naively — re-relaxation can happen; check distance vs current best.

## Problems
- [[Cheapest Flights Within K Stops]]
- [[Minimum Obstacle Removal to Reach Corner of Grid]]
- [[Minimum Edge Toggles on a Tree]]
- [[Critical Connections in a Network]]
