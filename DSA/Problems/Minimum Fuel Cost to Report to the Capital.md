---
pattern: tree-traversal
family: tree
difficulty: medium
leetcode: 2477
time: O(n)
space: O(n)
tags: [dsa, tree]
---
# Minimum Fuel Cost to Report to the Capital
[LC 2477](https://leetcode.com/problems/minimum-fuel-cost-to-report-to-the-capital/) · [[Tree - Traversal]]

## Recognize
- Tree + "everyone travels to the root, cars hold `seats`, minimize fuel"
- Looks like routing/flow — actually collapses to a per-edge argument
- "Think per edge, not per traveler": every subtree resident must cross the subtree's parent edge

## Insight
For each edge, number of cars needed = `(nodes in subtree + seats - 1) / seats`. Everyone below an edge must cross it, and carpooling caps at `seats` per car, so that edge costs exactly `ceil(p / seats)` liters. Sum over all non-root edges. One post-order DFS returning subtree sizes.

## Approach
DFS from root 0. Each node returns its subtree population (`1 +` children's sizes). For each child edge, add `ceil(childSize / seats)` to a global `long long` accumulator. Root contributes no edge.

## Pitfalls
- **Ceiling division is `(p + s - 1) / s`** — not `(p - s + 1) / s`. Check with p=5, s=2: should be 3.
- **Accumulate the subtree size**: `cnt += dfs(child)`. Forgetting it makes every node report size 1 — and it still *passes star-shaped examples* (where subtrees genuinely are size 1). Test with a deep path, not just a star.
- **`long long` accumulator**: path graph with `seats = 1` costs ~n²/2 ≈ 5·10^9 at n = 10^5. An `int ans` wraps even if the function returns `long long`.
- n = roads.size() + 1 (tree edge count), not roads.size().

## Similar
[[Sum of Distances in Tree]]
