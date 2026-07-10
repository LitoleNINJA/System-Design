---
pattern: dp-bitmask
family: dp
difficulty: hard
leetcode: 1434
time: O(40 · 2^n · n)
space: O(40 · 2^n)
tags:
  - dp
---
# Number of Ways to Wear Different Hats to Each Person
[LC 1434](https://leetcode.com/problems/number-of-ways-to-wear-different-hats-to-each-person/) · [[Bitmask DP]] · [[Counting DP]]

## Recognize
Up to 40 hats, ≤ 10 people. Each person has a list of acceptable hats. Count assignments where every person gets a distinct hat.

## Insight
Bitmask on the **smaller side**. Hats (40) don't fit in a mask; people (≤ 10) fit comfortably in 2¹⁰ = 1024. Iterate over hats, mask = which people are assigned.

## Approach
Group: for each hat `h`, list which people like it.
`dp(h, mask)` — number of ways to assign hats `h..40` such that the people in `mask` are still unassigned.
- **Skip hat `h`:** `dp(h+1, mask)`
- **Assign hat `h` to person `p` (if `p` is in mask and likes `h`):** `dp(h+1, mask without p)`

Sum both. Base: `mask == 0` → `1`. Initial: `mask = (1 << n) − 1`.

## Pitfalls
- Iterating on people instead of hats blows the mask up to 2⁴⁰.
- Per-person hat list must be inverted to a per-hat people list before the DP.
- Mod every addition.

## Similar
[[Smallest Sufficient Team]] · [[Find the Shortest Superstring]]
