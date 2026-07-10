<%*
const name = await tp.system.prompt("Pattern name (e.g. Knapsack DP)");
await tp.file.rename(name);
const family = await tp.system.suggester(
  ["DP","Graph","Tree","String","Math","Array","Greedy"],
  ["dp","graph","tree","string","math","array","greedy"],
  false, "Family"
);
-%>
---
type: pattern
name: <% name %>
family: <% family %>
tags: [pattern, <% family %>]

---
# <% name %>

## Signature


## Recognize
- 

## Canonical state / structure


## Transition / steps


## Variants
- 

## Pitfalls
- 

## Problems