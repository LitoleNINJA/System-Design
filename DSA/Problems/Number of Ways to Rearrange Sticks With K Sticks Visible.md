---
pattern: dp-permutation
family: dp
difficulty: hard
leetcode: 1866
time: O(n·k)
space: O(n·k)
tags:
  - dp
---
# Number of Ways to Rearrange Sticks With K Sticks Visible
[LC 1866](https://leetcode.com/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/) · [[Permutation DP]]

## Recognize
Count permutations of distinct heights where exactly `k` are visible from the left (each visible stick taller than all before it).

## Insight
Decide for the **shortest** stick — it never blocks anything, so the subproblem stays clean.

## Approach
`dp(i, k)` — ways to arrange `i` sticks with `k` visible.
- **Place at start (visible):** `dp(i−1, k−1)`
- **Place anywhere else (hidden behind any of the `i−1` others):** `dp(i−1, k) · (i−1)`

Base: `dp(0, 0) = 1`. Answer: `dp(n, k)`.

## Pitfalls
- Picking the *tallest* stick first would entangle the case analysis — shortest is the clean choice.
- The `(i−1)` multiplier captures the count, no need to track *where* it hides.
- Modulo on the multiplication.

## Similar
[[K Inverse Pairs Array]] · [[Count Vowels Permutation]]
