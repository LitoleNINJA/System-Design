---
pattern: tree-traversal
family: tree
difficulty: hard
leetcode: 834
time: O(n)
space: O(n)
tags:
  - tree
---
# Sum of Distances in Tree
[LC 834](https://leetcode.com/problems/sum-of-distances-in-tree/) · [[Tree - Traversal]]

## Recognize
- "For every node in the tree, compute X" — brute force is one DFS per root = O(n²)
- The answer at adjacent nodes is *related* in O(1) — a candidate for rerooting

## Insight
When we move from `par` to `child`, nodes in `child`'s subtree come closer by 1, and the others go away by 1. So `ans[child] = ans[par] + n - 2*size[child]`. Two DFSes — one to anchor `ans[0]` and subtree sizes, one to propagate everywhere else.

## Approach
**Pass 1 (post-order DFS from root 0):** for each child `v` of `u`:
- recurse first
- `dist[u] += dist[v] + size[v]` — each of `v`'s subtree nodes contributes one extra `u–v` edge on top of its distance to `v`
- `size[u] += size[v]`

After pass 1, `dist[0]` is the root's answer; `size[v]` is `v`'s subtree size when rooted at 0.

**Pass 2 (top-down DFS from root 0):** for each child `v` of `u`:
- `dist[v] = dist[u] + n - 2*size[v]`
- recurse into `v`

## Pitfalls
- In pass 1, the contribution to `dist[u]` uses `size[v]` (the child's subtree), **not** `size[u]` (which keeps mutating as siblings get absorbed). Using `size[u]` silently undercounts.
- Pass 2 must call itself recursively — copy-pasting the function body and forgetting to rename the recursive call to `dfs2` re-runs pass 1, clobbering `size[]` and re-polluting `dist[]`.
- Pass 2 must be **top-down (pre-order)** — parent's answer must be finalized before the child reads it.
- Initialize `size[v] = 1` (the node itself) before accumulating.
- For undirected adj lists, skip the parent edge to avoid double traversal.
