---
pattern: greedy
family: greedy
difficulty: hard
leetcode: 1326
time: O(n) with jump array (O(m^2) worst case for sort + nested scan)
space: O(n)
tags: [dsa, greedy]
---
# Minimum Number of Taps to Open to Water a Garden
[LC 1326](https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/) · [[Greedy]]

## Recognize
- "Cover the whole segment [0, n] using the fewest items, each item is an interval"
- Each element maps to an interval → minimum interval cover
- Reframes exactly into **Jump Game II**

## Insight
For each interval, we try to find the best next interval. Tap `i` → interval `[max(i-r, 0), min(i+r, n)]`. Now it's "min intervals to cover [0, n]" = **Jump Game II**: build `jump[L] = max reach from left end L`, sweep tracking `curEnd` / `farthest`, bump the counter when forced to commit.

## Approach
Two equivalent greedies:
1. **Jump array (canonical, O(n)):** `jump[max(i-r,0)] = max(jump[...], i+r)` clamped to n. Sweep left→right: `farthest = max(farthest, jump[x])`; when `x == curEnd`, commit a tap (`ans++`, `curEnd = farthest`). If `curEnd` can't advance before `x` reaches it → return -1.
2. **Sort + nested jump (this submission):** sort intervals by left asc (right desc on tie). Start from the left=0 interval, repeatedly scan all intervals starting within the current right and jump to the one with the farthest right. O(m^2) worst case.

## Pitfalls
- **Clamp both ends**: `i - r` can go negative, `i + r` can exceed `n`. Forgetting either breaks coverage logic.
- **-1 detection**: failure is when, before the sweep position reaches the current covered end, no reachable interval extends it. Equivalently `farthest == curEnd` and still `< n`.
- **Continuous coverage**: the garden is the real segment `[0, n]`, not integer points. The next interval must start `<= current right` (touching is fine); any gap → -1. Use `<=`, not `<`.
- The first tap must cover position 0 — tap 0 always gives an interval with left 0, so a valid start always exists.
- Sort + nested scan is O(m^2) worst case (many shared left endpoints). The `jump[]` sweep is the clean O(n).
