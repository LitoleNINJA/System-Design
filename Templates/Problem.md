<%*
const title = await tp.system.prompt("Problem title");
await tp.file.rename(title);

const patternLabels = [
  "Knapsack DP","Partition DP","Counting DP","Interval DP","Grid DP",
  "Bitmask DP","Permutation DP","Game Theory DP","DP - LIS/LCS/LPS",
  "DP - String Match","DP - Math","DP on DAG","DP + Binary Search",
  "Two Pointers","Sliding Window","Binary Search","Prefix Sum",
  "Stack/Monotonic","Heap","Greedy","Backtracking","Bit Manipulation",
  "Graph - BFS/DFS","Graph - DSU","Graph - Shortest Path","Graph - Topo Sort",
  "Graph - MST","Tree - Traversal","Tree - LCA","Trie","Segment Tree",
  "Fenwick Tree","String - KMP","String - Z","String - Suffix Array",
  "Math - Number Theory","Math - Combinatorics"
];
const patternSlugs = [
  "dp-knapsack","dp-partition","dp-counting","dp-interval","dp-grid",
  "dp-bitmask","dp-permutation","dp-game","dp-sequence",
  "dp-string","dp-math","dp-dag","dp-binsearch",
  "two-pointers","sliding-window","binary-search","prefix-sum",
  "stack-monotonic","heap","greedy","backtracking","bits",
  "graph-traversal","graph-dsu","graph-shortest-path","graph-topo",
  "graph-mst","tree-traversal","tree-lca","trie","segment-tree",
  "fenwick","string-kmp","string-z","string-suffix-array",
  "math-number-theory","math-combinatorics"
];
const familyFor = (slug) => {
  if (slug.startsWith("dp-")) return "dp";
  if (slug.startsWith("graph-")) return "graph";
  if (slug.startsWith("tree-")) return "tree";
  if (slug.startsWith("string-")) return "string";
  if (slug.startsWith("math-")) return "math";
  return slug.split("-")[0];
};

const pattern = await tp.system.suggester(patternLabels, patternSlugs, false, "Pattern");
const patternDisplay = patternLabels[patternSlugs.indexOf(pattern)];
const family = familyFor(pattern);

const difficulty = await tp.system.suggester(["Easy","Medium","Hard"], ["easy","medium","hard"], false, "Difficulty");
const lc = await tp.system.prompt("LeetCode # (blank if none)", "");
const time = await tp.system.prompt("Time complexity", "O(n)");
const space = await tp.system.prompt("Space complexity", "O(n)");
-%>
---
pattern: <% pattern %>
family: <% family %>
difficulty: <% difficulty %>
leetcode: <% lc %>
time: <% time %>
space: <% space %>
tags: [dsa, <% family %>]

---
# <% title %>
<% lc ? `[LC ${lc}](https://leetcode.com/problems/) · ` : "" %>[[<% patternDisplay %>]]

## Recognize

## Insight

## Approach

## Pitfalls
- 
## Similar