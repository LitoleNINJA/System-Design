---
pattern: dp-grid
family: dp
difficulty: hard
leetcode: 1444
time: O(r·c·k·(r+c))
space: O(r·c·k)
tags:
  - dp
---
# Number of Ways of Cutting a Pizza
[LC 1444](https://leetcode.com/problems/number-of-ways-of-cutting-a-pizza/) · [[Grid DP]] · [[Counting DP]]

## Recognize
Cut a pizza into `k` pieces with horizontal/vertical cuts. Each given-away piece must contain ≥ 1 apple. Count valid sequences.

## Insight
After each cut, the remaining piece is always a **bottom-right subgrid** `[r..rows−1][c..cols−1]`. State collapses to `(r, c, k)` — no rectangle bookkeeping needed. 2D prefix sums make "has apple?" checks O(1).

## Approach
Precompute 2D prefix sums of apples.
`dp(r, c, k)` — number of ways to cut the subgrid `[r..][c..]` into `k` pieces.
- Horizontal cut at row `x` (give away `[r..x−1][c..]`, keep `[x..][c..]`): recurse `dp(x, c, k−1)` if the given piece has ≥ 1 apple.
- Vertical cut at col `x`: recurse `dp(r, x, k−1)` if the given piece has ≥ 1 apple.

Base: `k == 1` → `1` if remaining subgrid has ≥ 1 apple, else `0`.

## Pitfalls
- "Has apple" is a per-subrectangle check — use 2D prefix sums, never global row/col flags.
- Mod every addition.

## Similar
[[Number of Ways to Stay in the Same Place After Some Steps]]
