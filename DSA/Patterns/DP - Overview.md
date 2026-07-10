---
type: pattern
name: DP - Overview
family: dp
tags: [pattern, dp]
---
# DP — Overview

**State** = what I need to *remember*. **Transition** = what I can *forget*.

## Families

| Family | Signature | Recurrence style |
|---|---|---|
| [[Knapsack DP]] | Choose subset, optimize under capacity | `skip vs take with cost` |
| [[Partition DP]] | Split ordered sequence into k contiguous groups | optimize over split point `j` |
| [[Counting DP]] | Count arrangements under constraints | `Σ (choices × subproblem)` |
| [[Interval DP]] | Cost of merging/splitting ranges | `dp(i,j) from dp(i,k) + dp(k,j)` |
| [[Grid DP]] | Navigate grid with constraints | `dp(i,j) from neighbors` |
| [[Bitmask DP]] | Subset/visited-set state, small n (≤20) | iterate mask, transition by 1 bit |
| [[Permutation DP]] | Arrange elements with ordering rules | pick extreme element, count placements |
| [[Game Theory DP]] | Two-player minimax | current = best − opponent's best |

## Top-down vs Bottom-up

- **Top-down** when state space is large but **sparse** (many unreachable states), or recurrence has complex bounds making loop ordering tricky.
- **Bottom-up** when state space is **dense**, or you want space optimization (rolling array).
- Top-down can TLE past ~10⁶ iterations from recursion overhead + stack depth. Switch to bottom-up.
- State doesn't fit array index (too large or negative) → `unordered_map<>` memo.

## Recurring frameworks

- **Threshold cap.** State dim tracks a quantity but you only care if it crosses a threshold → cap at threshold.
- **Permutation DP.** Pick the extreme element that interacts least with others — smallest or largest.
- **State-vs-multiplier.** If a dimension doesn't change the *structure* of future decisions, it's a multiplier, not a state.
- **Coordinate compress.** When a state dim has only a few distinct realizable values (out of a huge range), map them to small ids.

## Recurring bugs

- Wrong sentinel values (`-1` as both "not computed" and a valid answer)
- Modulo applied outside `min`/`max`
- `INT_MAX` overflow on sums — use `long long` or guard
- `memset` on non-`int` types — only works for 0 and -1
- Off-by-one on partition endpoints
- Large DP arrays on the stack → segfault, move to heap

## Sequence DP shorthand

- **LIS** O(n log n): maintain ascending `d[]`, `upper_bound` replace or append.
- **LPS** O(n²): `dp[i][j] = s[i]==s[j] ? dp[i+1][j-1]+2 : max(dp[i+1][j], dp[i][j-1])`.
- **LCS** O(n·m): `dp[i][j] = s[i]==t[j] ? dp[i-1][j-1]+1 : max(dp[i-1][j], dp[i][j-1])`.
