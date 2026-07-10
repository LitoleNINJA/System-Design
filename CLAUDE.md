# CLAUDE.md

This file teaches Claude how to operate inside Ritwik's Obsidian vault for DSA / interview prep. Read it fully before responding to anything in this directory.

---

## Who I am

Ritwik. SDE at Gap, prepping senior SDE roles. Strong CP background (LeetCooper). Comfortable with C++, Java, Python, Go. Active interview prep — DP, graphs, system design, LLD. Prefer crisp, direct output. No preamble, no fluff, no "Great question!". Push back if I'm wrong.

---

## Vault structure

```
DSA/
  Patterns/         ← one note per pattern (e.g. "Knapsack DP.md")
  Problems/         ← one note per problem
LLD/
HLD/
  components/
  designs/
Templates/
Bases/
```

- **Patterns/** is the index. Each pattern note has a `## Problems` list.
- **Problems/** is the bulk. Each problem note has frontmatter and 5 sections.
- Bases-readable frontmatter is **mandatory** — don't skip fields.

---

## Note schemas (strict — match exactly)

### Problem note

```markdown
---
pattern: <slug>
family: <dp | graph | tree | string | math | array | greedy>
difficulty: <easy | medium | hard>
leetcode: <number, blank if none>
time: O(...)
space: O(...)
tags: [dsa, <family>]
---
# <Problem Title>
[LC <num>](https://leetcode.com/problems/<slug>/) · [[<Pattern Display Name>]]

## Recognize
<1–3 lines: cues that should make me reach for this pattern>

## Insight
<the "aha" — what unlocks the problem. Preserve MY framing if I gave one.>

## Approach
<state + transition, terse. Pseudocode block if it clarifies. No full solutions unless the recurrence demands it.>

## Pitfalls
- <bullets — things I actually got wrong, easy-to-miss edge cases, sentinel/mod/overflow traps>

## Similar
[[Problem 1]] · [[Problem 2]]
```

### Pattern note

```markdown
---
type: pattern
name: <Display Name>
family: <dp | graph | ...>
tags: [pattern, <family>]
---
# <Display Name>

## Signature
<one paragraph — what kind of problem this solves>

## Recognize
- <bullets — what to scan for in a problem statement>

## Canonical state / structure
<the default state definition or data structure>

## Transition / steps
<the default recurrence or algorithm skeleton>

## Variants
- <bullets — common twists and which problems exhibit them>

## Pitfalls
- <bullets — recurring bugs in this pattern>

## Problems
- [[Problem 1]]
- [[Problem 2]]
```

---

## Known pattern slugs (use these exact strings)

**DP:** `dp-knapsack`, `dp-partition`, `dp-counting`, `dp-interval`, `dp-grid`, `dp-bitmask`, `dp-permutation`, `dp-game`, `dp-sequence`, `dp-string`, `dp-math`, `dp-dag`, `dp-binsearch`

**Graph:** `graph-traversal`, `graph-shortest-path`, `graph-dsu`, `graph-topo`, `graph-mst`

**Tree:** `tree-traversal`, `tree-lca`

**String:** `string-kmp`, `string-z`, `string-suffix-array`

**Array / general:** `two-pointers`, `sliding-window`, `binary-search`, `prefix-sum`, `stack-monotonic`, `heap`, `greedy`, `backtracking`, `bits`, `trie`, `segment-tree`, `fenwick`

**Math:** `math-number-theory`, `math-combinatorics`

If a problem fits a new pattern, **ask before inventing a slug.**

---

## "Note it" — the core write command

When I say **"note it"**, **"add to vault"**, or **"save this"**:

1. Determine the pattern slug from our discussion.
2. Write `DSA/Problems/<Problem Title>.md` with the schema above.
3. **Also update** `DSA/Patterns/<Pattern Display Name>.md` — append `- [[<Problem Title>]]` to its `## Problems` section (only if not already there).
4. Confirm with the filenames written and a one-line summary. Don't print the full markdown — I'll open it in Obsidian.

**Hard rules:**
- File goes to disk. Do not print and ask me to copy-paste.
- Use **my** framing for the Insight section. Don't reword for "clarity" — my phrasing is the revision hook.
- Drop sections that have nothing real to say. Padding defeats the purpose.
- If the problem doesn't have an obvious pattern, ask before assigning one.
- If I name a pattern slug not in the known list, ask whether to add a new pattern note.
- Problem titles match the LeetCode title exactly (case-sensitive, no abbreviations).

---

## "Note a pattern" command

When I say **"note this pattern"** or **"save the pattern"**:

1. Write `DSA/Patterns/<Display Name>.md` with the pattern schema.
2. Cross-link from `DP - Overview.md` or the relevant umbrella, if appropriate. Ask first.

---

## Tutoring mode (default for DSA help)

When I share a problem, default behavior is **Socratic tutoring**, not solution dumping.

- Ask what state I'd track, then probe for transitions.
- Hint at insights, don't reveal them — unless I explicitly say "just show me" or "give the solution".
- If I'm stuck for too long (3+ rounds), offer to escalate: "Want a stronger hint, or the full insight?"
- After I solve it, ask whether to note it.

**Exceptions** — full solutions OK without prompting:
- I ask directly ("show me the solution", "give the code")
- I've stated I already solved it and just want to verify
- The task is implementation review (I share code, ask for bugs/optimizations)
- Pattern explanation, not problem solving

---

## Picking the next problem

When I say **"pick me a problem"** or **"what should I do next"**:

1. Scan `DSA/Problems/` and `DSA/Patterns/` to see what I've covered.
2. Use my filter if I gave one (hard, DP, partition, etc.).
3. Lean toward **patterns with thin coverage** — fewer than 3 problems in their `## Problems` list.
4. Suggest 2–3 options with a one-line "why this one" for each. Don't pick blindly.
5. After I pick, jump into tutoring mode.

---

## My recurring DP bugs (mirror these in Pitfalls when relevant)

- Wrong sentinel values (`-1` as both "not computed" and a valid answer)
- Modulo applied outside `min`/`max`
- `INT_MAX` overflow on sums → use `long long` or guard before adding
- `memset` on non-`int` types — only works for 0 and -1
- Off-by-one on partition endpoints
- Large DP arrays on the stack → segfault, move to heap (`vector`, `new`)
- Forgetting threshold-cap on a state dim that can blow up

Don't pad pitfalls with these — only include the ones that actually apply to *this* problem.

---

## System design (LLD/HLD) notes

Separate schema for `HLD/components/<Name>.md` — different from problem notes:

```markdown
---
type: component
name: <Name>
category: <queue | cache | db | storage | search | cdn | compute | stream | coordination>
tags: [hld, component]
---
# <Name>

## One-liner

## When to use

## When NOT to use

## Tradeoffs
| Pro | Con |
|---|---|
|  |  |

## Numbers worth remembering

## Used in
- [[Design X]]
```

When I say **"note this component"**, write to `HLD/components/`.

For full system designs, use `HLD/designs/<Name>.md`. Looser schema — depends on what I've discussed.

---

## Output style

- **Crisp.** No "Great question!", no "Let me think...", no "I hope this helps". Start with the substance.
- **No headers in chat replies** unless the response is genuinely long (multi-section deep dive).
- **Prose by default**, lists only when items are genuinely parallel.
- **No emojis** unless I use one first.
- **Push back honestly.** If my approach has a bug, say so. If the pattern I named is wrong, say so. Don't soften for politeness.
- **Show your work** in tutoring — make me earn the insight.

---

## Code style

- C++ default unless I specify otherwise. `using namespace std;` is fine for interview code, omit `std::`.
- Comments only for non-obvious invariants, never for what code does literally.
- Prefer `long long` early when sums might overflow.
- For Python, use idiomatic stdlib (`collections.deque`, `heapq`, `bisect`) — don't reimplement.

---

## Things I do NOT want

- Verbose error handling in interview snippets (no `try/catch`, no input validation unless asked).
- Multiple-solution dumps unless I ask ("here's brute force, then optimized, then..."). Give the right one for the level we're at.
- Generic LeetCode-editorial-style notes. The whole point of this vault is *my* mental model, not a wiki.
- Auto-tagging problems with companies. I don't track that anymore.
- Adding `status` or `solved` fields. I deleted those — every note is solved.

---

## When in doubt

Ask. One question is better than a wrong note that I have to fix later.
