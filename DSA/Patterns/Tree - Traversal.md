---
type: pattern
name: Tree - Traversal
family: tree
tags: [pattern, tree]
---
# Tree — Traversal

## Signature
DFS-based algorithms on trees: subtree summaries, two-pass rerooting, leaf-inward peeling. The shared move — exploit acyclicity so a single DFS gives a clean post-order or pre-order layering, and avoid recomputation by relating adjacent nodes' answers.

## Recognize
- "For every node, compute X" with naive O(n²) brute force (one DFS per node) → candidate for **rerooting**
- "X over the whole tree" with subtree structure → **post-order subtree DP**
- Each leaf has a forced choice that propagates inward → **leaf-inward peeling**
- Adjacent-node answers related in O(1) → rerooting is almost certainly the technique

## Canonical state / structure
- `size[v]` — subtree size when rooted at 0
- `dp[v]` / `dist[v]` — accumulated quantity over v's subtree
- Computed in **post-order** (children before parent) for upward summaries; **pre-order** (parent before children) for downward propagation

## Transition / steps
- **Post-order subtree DP** — `dp[u] = f(dp[c_1], ..., dp[c_k], values[u])` after all children visited.
- **Rerooting (two-pass)** — pass 1 (post-order) computes everything assuming root 0; pass 2 (pre-order) propagates `ans[child] = g(ans[par], size[child], ...)` to every other node in O(1) per edge.
- **Leaf-inward peeling** — repeatedly remove leaves, decide their forced state, propagate inward.
- **Parent-skipping** — in undirected adj lists, pass `par` and skip it to avoid revisits.

## Variants
- **Rerooting for sum of distances** — `ans[child] = ans[par] + n - 2·size[child]`. (Sum of Distances in Tree)
- **Tree DP with two states per node** (e.g., "include u" vs "exclude u").
- **Tree diameter (two DFS)** — DFS from any node to find farthest `u`, then DFS from `u` for the diameter.
- **Subtree XOR / sum / min** — pure post-order accumulation.

## Pitfalls
- Rerooting pass 1: using the *running* aggregate (`size[u]` mid-loop) where the child's snapshot (`size[v]`) was meant. Silent undercount.
- Pass 2 must be **pre-order** — parent finalized before child reads.
- Two-pass code: the recursive call in pass 2 must call pass 2, not pass 1 (easy copy-paste bug — clobbers state).
- For undirected trees, always skip `par` in the neighbor loop.
- `size[v]` after pass 1 is fixed as "subtree size when rooted at 0" — pass 2's formula uses *that* fixed value, even though conceptually the root is moving.

## Problems
- [[Sum of Distances in Tree]]
- [[Minimum Fuel Cost to Report to the Capital]]
