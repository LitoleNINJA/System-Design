---
type: pattern
name: Counting DP
family: dp
tags: [pattern, dp]
---
# Counting DP

## Signature
Count the number of arrangements / sequences / structures satisfying given constraints.

## Recognize
- "Number of ways to ..."
- Output is a count, usually `mod 10⁹+7`
- Constraints can be encoded as state dimensions

## Canonical state / structure
`dp(state)` — number of valid completions from `state` onward. Add contributions from each branch instead of taking min/max.

## Transition / steps
```
dp(state) = Σ (choice multiplier) × dp(next_state)
```
Each transition is a *choice × subproblem* product, summed.

## Variants
- **State-vs-multiplier.** Dim that doesn't change future structure → fold into multiplier, drop from state. (Music Playlists: don't track *which* song, just count distinct songs used.)
- **Prefix-sum window.** Inner sum across a sliding range → maintain prefix sums in O(1) per state. (K Inverse Pairs)
- **Per-index map memo.** State dim too large/negative for array → `map<>` per outer index. (Arithmetic Slices II)
- **Transition table.** Allowed next-states encoded as a table; loop transitions instead of if-else. (Count Vowels Permutation)

## Pitfalls
- Modulo must wrap **every** addition and multiplication, not just the final result.
- Negative modulo from subtraction: `((a − b) % mod + mod) % mod`.
- Counting from scratch vs counting completions — pick one convention and stick to it.
- `dp[base] = 1` (one empty completion), not `0`.

## Problems
- [[Number of Music Playlists]]
- [[Number of Ways to Form a Target String Given a Dictionary]]
- [[K Inverse Pairs Array]]
- [[Arithmetic Slices II - Subsequence]]
- [[Count Vowels Permutation]]
- [[Number of Ways to Stay in the Same Place After Some Steps]]
- [[Number of Ways of Cutting a Pizza]]
- [[Number of Ways to Wear Different Hats to Each Person]]
- [[Number of Ways to Rearrange Sticks With K Sticks Visible]]
- [[Profitable Schemes]]
