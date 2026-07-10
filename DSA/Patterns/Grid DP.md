---
type: pattern
name: Grid DP
family: dp
tags: [pattern, dp]
---
# Grid DP

## Signature
Navigate a 2D grid with constraints. State = position (+ extra dims for collectibles, agents, direction, etc.).

## Recognize
- "Path in a grid", "robot starts at...", "collect cherries"
- Movement from a fixed neighbor set (down, right, etc.)
- Per-cell value or constraint

## Canonical state / structure
`dp(i, j)` — best from cell `(i, j)` to destination. Or position + extra (agent index, k items left).

## Transition / steps
- **Standard:** `dp(i, j) = cell(i, j) + min/max(dp from valid neighbors)`
- **Two agents on same grid:** loop over the cross product of moves, e.g. `dp(i, j1, j2)` with 3×3 = 9 direction pairs per step.

## Variants
- **Two-smallest optimization** — when transition is "any neighbor except this column", track top-2 best values per row. (Min Falling Path Sum II)
- **Bottom-right subgrid invariant** — after each cut, remaining region is `[r..rows][c..cols]` → 2D prefix sums make region queries O(1). (Cutting a Pizza)
- **Position-bound trimming** — cap reachable positions by the remaining steps so you have time to return. (Stay in Same Place After Some Steps)

## Pitfalls
- Both agents on the same cell — count only once.
- Bounds: skip transitions where the neighbor falls outside the grid.
- Bottom-up direction (top→bottom or bottom→top) must match where the answer lives.

## Problems
- [[Cherry Pickup II]]
- [[Minimum Falling Path Sum II]]
- [[Number of Ways of Cutting a Pizza]]
- [[Number of Ways to Stay in the Same Place After Some Steps]]
