---
pattern: tree-traversal
family: graph
difficulty: hard
leetcode: 3329
time: O(n)
space: O(n)
tags:
  - graph
  - tree
---
# Minimum Edge Toggles on a Tree
[LC 3329](https://leetcode.com/problems/minimum-edge-toggles-to-make-tree-z-good/) · [[Graph - BFS DFS]]

## Recognize
Tree problem where each leaf has a forced local decision; the rest cascades. "Leaf-inward" or "peel from leaves" approach.

## Insight
At a leaf, there's no choice — its only option is determined. Once decided, peel the leaf, and the parent now has a leaf-like constraint. Greedy + DFS from leaves.

## Approach
DFS post-order. Return per-subtree count of toggles needed. At each node, combine children's results and decide locally.

```
def dfs(u, parent):
    cost = 0
    for v in children(u):
        cost += dfs(v, u)
        // local decision: flip edge (u, v) if children's state demands
    return cost
```

## Pitfalls
- The local rule depends on the specific toggle/labeling condition — read carefully before generalizing.
- Tree DP from leaves = `dfs` returns subtree summary, parent uses it.
- Don't try to BFS this — the dependency is bottom-up.

## Similar
[[Critical Connections in a Network]]
