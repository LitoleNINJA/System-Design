---
pattern: graph-shortest-path
family: graph
difficulty: hard
leetcode: 2577
time: O(nm log(nm))
space: O(nm)
tags: [dsa, graph]
---
# Minimum Time to Visit a Cell In a Grid
[LC 2577](https://leetcode.com/problems/minimum-time-to-visit-a-cell-in-a-grid/) · [[Graph - Shortest Path]]

## Recognize
- Grid + "minimum time to reach corner" but entering a cell has a *time gate* (`grid[r][c]`)
- Move cost isn't uniform → not plain BFS; entry time depends on `max(arrival, gate)` → Dijkstra
- The waiting/oscillation trick hidden behind a parity constraint

## Insight
You can burn time by stepping back and forth between two visited cells → you can always waste an **even** number of seconds. Time at `(r,c)` always shares parity with `(r+c)` (each step flips both). So earliest legal entry into a neighbor = `max(t+1, grid[nr][nc])`, bumped by 1 if its parity is wrong. Dijkstra ordered by entry time.

## Approach
1. **Stuck check:** if both neighbors of the start need time `> 1` (`grid[0][1] > 1 && grid[1][0] > 1`), you can never make a first move → return -1. (A neighbor with gate 1 is usable: first move is at time 1.)
2. Dijkstra from `(0,0)` at time 0. Relaxing neighbor `(nr, nc)` from time `t`:
   - `cand = max(t + 1, grid[nr][nc])`
   - if `cand % 2 != (nr + nc) % 2`, `cand++` (oscillate one extra second to fix parity)
   - relax if `cand < dist[nr][nc]`.
3. Answer = `dist[m-1][n-1]`.

## Pitfalls
- **Relaxation direction**: update when the new time is *smaller* (`cand < dist`). Writing `if (dist > ...) continue;` with a fresh `1e9` dist skips every neighbor and returns -1 for everything.
- **`cand` is an absolute time**, already built from `max(t+1, gate)`. Don't add the popped `d` again (`d + cand` double-counts).
- **Parity bump fires on mismatch**: increment when `cand % 2 != (nr+nc) % 2`. The `==` version increments the *correct* cells and breaks the wrong ones — a brutal "almost right" WA.
- **The -1 case needs an explicit start check** — Dijkstra alone will "enter" a far neighbor at its gate value as if you could wait at the start (you can't, with nothing to oscillate against), giving a finite-but-wrong answer.
- Standard Dijkstra stale-skip (`if (d > dist[i][j]) continue;`) still required.
