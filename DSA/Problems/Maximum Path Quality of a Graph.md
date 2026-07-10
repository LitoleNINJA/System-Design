---
pattern: backtracking
family: graph
difficulty: hard
leetcode: 2065
time: O(deg^depth) ≈ O(4^10)
space: O(n) for visited + recursion stack
tags:
  - graph
---
# Maximum Path Quality of a Graph
[LC 2065](https://leetcode.com/problems/maximum-path-quality-of-a-graph/) · [[Backtracking]]

## Recognize
- "Max value path starting and ending at node 0, time-bounded"
- Each visited node's value counted once → state looks DP-shaped with a visited subset
- Tight constraints: `time[i] >= 10`, `maxTime <= 100`, `deg(u) <= 4`

## Insight
Max path len <= 10 because time[i] >= 10 and maxTime <= 100. Visited set looks DP-shaped but 2^n kills memo. Search tree is tiny — branching <= 4, depth <= 10, so ~4^10 ≈ 10^6 paths. Brute-force DFS with backtracking wins. **Constraints redirect you AWAY from DP.**

## Approach
DFS state: `(u, timeLeft, curSum, visited)`. On entering `u`:
- If `u` not in `visited`: insert, `curSum += a[u]`, set `flag = true`.
- If `u == 0`: update `ans` with `curSum`.
- Recurse on each neighbor `v` with edge `w` if `w <= timeLeft`. **No revisit guard.**
- On exit: if `flag`, erase `u` and undo `curSum`.

The call that first adds `u` is the one that removes it on backtrack; later occurrences of `u` on the same path are transparent w.r.t. the set.

## Pitfalls
- Guarding recursion with `!visited[v]` forbids revisits and silently kills every path that returns to 0 — only the trivial `ans = values[0]` survives.
- Update `ans` only at `u == 0` (path must start AND end at 0), not at every node.
- Passing the visited set by value makes the explicit `erase` on backtrack dead code (and copies the set per call). Pass by reference and erase, or by value and drop the erase.
- Don't double-add `values[u]` when `u` is a revisit — the `flag` handles this.
- `int ans;` as a class member is UB if not initialized — write `int ans = 0;`.
- For `n <= 50`, prefer `vector<bool> visited` over `unordered_set<int>` — faster and reads cleaner.
