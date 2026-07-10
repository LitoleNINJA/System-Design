---
type: pattern
name: Heap
family: array
tags: [pattern, array]
---
# Heap

## Signature
Ordered incremental extraction: when you need the best (or k-th best) element of a space far too large to materialize — all subsets, all pairs, all paths — a heap walks the space lazily, best-first, surfacing exactly as many candidates as you pay for.

## Recognize
- "k-th largest/smallest" with tiny k against a huge or exponential candidate space
- Top-k maintenance over a stream
- Merging sorted streams / lists
- Best-first expansion where each candidate has a few "next" candidates, all no better than it (Dijkstra-shaped)

## Canonical state / structure
Heap entries are **self-contained states**: `(key, frontier info)` — each state carries everything needed to generate its own successors. No global shared bookkeeping; legality of a successor is a per-state question.

## Transition / steps
1. Seed the heap with minimal starting states.
2. Pop the best state — it is finalized (monotonicity: children's keys ≥ parent's key).
3. Push its children under a **build discipline** that generates every candidate exactly once (e.g., extend only past your own frontier index).
4. Repeat k times.

## Variants
- k-th smallest subset sum — [[Find the K-Sum of an Array]] (subset lattice walk; 2-children extend/replace trick gives O(k log k))
- k smallest pairs from two sorted arrays (LC 373) — frontier = index pair
- Merge k sorted lists — frontier = position in each list
- Two heaps for running median; lazy-deletion heaps for sliding windows

## Pitfalls
- States sharing global memory (a single "removed" list) — legality differs per state; each must carry its own frontier
- Non-monotone child keys (a child smaller than its parent) silently breaks pop order
- The rank that lives *outside* the heap (empty subset, zero-cost option) — off-by-ones and unguarded k=1
- Fan-out per pop: cap children at what k pops can ever reach, or use an O(1)-children construction

## Problems
- [[Find the K-Sum of an Array]]
