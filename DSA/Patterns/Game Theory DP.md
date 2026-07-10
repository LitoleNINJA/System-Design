---
type: pattern
name: Game Theory DP
family: dp
tags: [pattern, dp]
---
# Game Theory DP

## Signature
Two-player perfect-information game. Each player plays optimally. Compute outcome for the player to move.

## Recognize
- Alice / Bob alternate moves
- "Optimal play", "can win", "max score difference"
- State derivable from board / picks

## Canonical state / structure
`dp(state)` = best the **player to move** can achieve from `state`. Symmetry: whatever opponent then achieves, this player loses by exactly that.

## Transition / steps
- **Score diff:** `dp(state) = max over moves of: gain − dp(next_state)`
- **Win/lose:** current wins if **any** move leads to a state where opponent loses, or wins immediately.

## Variants
- **Mask-only state** — turn + running totals are derivable from the picked-mask. (Can I Win)
- **Prefix-sum collapse** — after merges, the score is always a prefix sum → state is just the next index to consider. (Stone Game VIII)

## Pitfalls
- Sign confusion — pick "score diff from current player" or "total of current player" and don't mix.
- Edge case: must take at least one move, or can pass — base case differs.
- Early prune: if even the best possible total < target, return false fast.

## Problems
- [[Can I Win]]
- [[Stone Game VIII]]
