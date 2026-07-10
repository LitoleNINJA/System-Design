---
type: pattern
name: Greedy
family: greedy
tags: [pattern, greedy]
---
# Greedy

## Signature
Build the answer by repeatedly making the locally-optimal choice, never reconsidering. Correct only when a local choice provably can't preclude a global optimum — usually proven by an exchange argument or a matroid/interval structure.

## Recognize
- "Minimum / maximum number of X to achieve Y" where items have a natural ordering
- Interval problems: cover a segment, select non-overlapping, merge, schedule
- "Farthest reach" / "earliest finish" / "smallest next" framing
- A sort on one key suddenly makes the choice obvious

## Canonical state / structure
- Sort by the decisive key (start, end, ratio, deadline).
- Sweep maintaining a frontier: current covered end, farthest reachable, last chosen endpoint, or a heap of candidates.

## Transition / steps
- **Interval covering / Jump Game II** — track `curEnd` and `farthest`; commit a step when the sweep hits `curEnd`, set `curEnd = farthest`. (Min Taps to Water a Garden)
- **Activity selection** — sort by end, take each interval whose start ≥ last taken end.
- **Exchange argument** — to justify: show swapping the greedy choice for any alternative never worsens the result.
- **Heap-backed greedy** — when "best next" changes as you consume items, keep a priority queue.

## Variants
- **Interval cover → Jump Game II** — map items to intervals, build a `jump[]` reach array, sweep in O(n). (Minimum Number of Taps to Open to Water a Garden)
- **Sort + two pointers / sweep** — many "fewest/most under a constraint" problems.
- **Greedy + heap** — recompute the best candidate lazily.

## Pitfalls
- Greedy is *wrong by default* — verify with an exchange argument or a known structural guarantee before trusting it.
- Continuous vs discrete coverage: touching intervals (`<=`) vs strict gaps (`<`). Re-read whether the domain is points or a segment.
- Clamp boundaries when mapping items to intervals (negatives, beyond-`n`).
- Impossibility detection: explicitly handle "can't extend the frontier" → return -1 / fail.
- Don't conflate "locally maximal each step" with "globally optimal" unless proven.

## Problems
- [[Minimum Number of Taps to Open to Water a Garden]]
