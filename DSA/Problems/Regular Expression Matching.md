---
pattern: dp-string
family: dp
difficulty: hard
leetcode: 10
time: O(n·m)
space: O(n·m)
tags:
  - dp
---
# Regular Expression Matching
[LC 10](https://leetcode.com/problems/regular-expression-matching/) · [[DP - Overview]]

## Recognize
Match `s` against pattern with `.` and `*`. `*` binds to the **preceding** char as a unit.

## Insight
Treat `p[j]p[j+1]` as a single unit whenever `p[j+1] == '*'`. Two transitions from there:
- Use **0 copies** → skip the pair: `dp(i, j+2)`
- Use **1+ copies** (if `p[j]` matches `s[i]`) → consume one from `s`, stay at `j`: `dp(i+1, j)`

## Approach
`dp(i, j)` — does `s[i..]` match `p[j..]`?
```
if p[j+1] == '*':
    dp(i, j) = dp(i, j+2)  // 0 copies
            or (matches(s[i], p[j]) && dp(i+1, j))  // 1+ copies
else:
    dp(i, j) = matches(s[i], p[j]) && dp(i+1, j+1)
```

Base: `j == m` → `i == n`. Don't return early on `i == n` — remaining pattern must all be `x*` pairs.

## Pitfalls
- Guard `i < n` before accessing `s[i]` in both branches.
- `j == m && i < n` → `false`. `j < m && i == n` → may still succeed if rest is all `x*`.
- "Match" includes `p[j] == '.'`.

## Similar
[[Scramble String]]
