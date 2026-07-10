### Top-Down vs Bottom-Up

- DP **State** = what I **need to** **_remember_** , **Transition** = what I can  _forget_

| Family          | Signature                                           | Recurrence Style                       |
| --------------- | --------------------------------------------------- | -------------------------------------- |
| **Knapsack**    | Choose a subset, optimize under capacity            | `skip vs take with cost`               |
| **Partition**   | Split fixed-order sequence into k contiguous groups | `optimize over split point j`          |
| **Counting**    | Count arrangements under constraints                | `sum of (choices × subproblem)`        |
| **Interval**    | Cost of merging/splitting ranges                    | `dp(i,j) from dp(i,k) + dp(k,j)`       |
| **Grid/Path**   | Navigate a grid with constraints                    | `dp(i,j) from neighbors`               |
| **Permutation** | Arrange elements with ordering rules                | Pick extreme element, count placements |
|                 |                                                     |                                        |

- Whenever a state dimension tracks a **quantity** and you only care about whether it crosses a **threshold**, **cap it at that threshold**.
- **Top-Down DP** can lead to **TLE** once iterations exceed **10⁶**, due to stack depth and recursion overhead. In such cases, switch to **Bottom-Up DP** to avoid stack limitations and improve performance.
- **Top-down** is better when the state space is large but **sparse** (many unreachable states). Also easier when the recurrence has complex bounds or conditions that make loop ordering tricky.
- **Bottom-up** is better when the state space is **dense** (most states are visited), or when you want to do **space optimization** (like rolling array on the previous row, as in the falling path sum problem).
- Sometimes it might be difficult to keep a **state** as an array index, as it might be **too large** or **negative**. So we use an **unordered_map** for such cases.
- In **Permutation DP** problems, try the **extreme element that interacts least** with others. Sometimes that's the smallest, sometimes the largest.

---
### Longest Increasing Subsequence (LIS) : `O(n log n)`
- Maintain an increasing array `d`.
- For each element:
	- If `a[i]` is greater than the last element, append it.
	- Otherwise, find the smallest element in `d` that is strictly greater than `a[i]` and replace it.
	- Use **binary search** to achieve `O(log n)` per operation.

```cpp
vector<int> d(n + 1, INF);
d[0] = -INF;

for (int i = 0; i < n; i++) {
    int l = upper_bound(d.begin(), d.end(), a[i]) - d.begin();
    if (d[l - 1] < a[i] && a[i] < d[l]) {
        d[l] = a[i];
    }
}

int ans = 0;
for (int l = 0; l <= n; l++) {
    if (d[l] < INF) {
        ans = l;
    }
}

return ans;
```

### Longest Palindromic Subsequence (LPS) : `O(n^2)`
- https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/description/
	- Answer is N - **LPS**
	- LPS can be found using DP in n^2
	- ```cpp
	  for(int i = 0; i < n; i++)
            dp[i][i] = 1;

        for(int len = 2; len <= n; len++) {
            for(int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if(s[i] == s[j])
                    dp[i][j] = dp[i+1][j-1] + 2;
                else
                    dp[i][j] = max(dp[i+1][j], dp[i][j-1]);
            }
        }
	  ```

### Longest Common Subsequence (LCS) : `O(n * m)`
- https://leetcode.com/problems/shortest-common-supersequence/description/
	- LCS will be common in the answer, we will all the other char which are not part of LCS in order that they appear. 
	- To get LCS string, we make DP table of strings instead of int:
	  ```cpp
	  vector<vector<string>> dp(n+1, vector<string>(m+1));
        for(int i=1; i<=n; i++) {
            for(int j=1; j<=m; j++) {
                if(s[i-1] == t[j-1])
                    dp[i][j] = dp[i-1][j-1] + s[i-1];
                else {
                    if(dp[i-1][j].size() > dp[i][j-1].size())
                        dp[i][j] = dp[i-1][j];
                    else
                        dp[i][j] = dp[i][j-1];
                }
            }
        }
	  ```
--- 
--- 

---


---
## Important Questions

- [https://leetcode.com/problems/number-of-music-playlists/](https://leetcode.com/problems/number-of-music-playlists/) — **Counting**
    - dp(i, j) : # of playlists of length i with j distinct songs
    - New song: `dp(i-1, j-1) * (n-j)` — don't track which song, just count
    - Repeat: `dp(i-1, j) * max(0, j-k)` — gap rule becomes a multiplier
- [https://leetcode.com/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/](https://leetcode.com/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/) — **Counting/Knapsack**
    - Actual words don't matter, only `freq[col][char]` matters
    - dp(i, k) : # of ways to make `t[0..i]` using columns `k..m-1`
    - Skip col: `dp(i, k+1)` | Take col: `dp(i+1, k+1) * freq[k][t[i]]`
- [https://leetcode.com/problems/profitable-schemes/](https://leetcode.com/problems/profitable-schemes/) — **Knapsack with threshold cap**
    - dp(i, n, p) : # of ways from groups `i..end` with `n` people used and `p` profit
    - Cap `p` at `minProfit` — once threshold hit, extra profit doesn't change future
    - Base: `i == numGroups` → return `p >= minProfit ? 1 : 0`
- [https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/](https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/) — **Partition DP**
    - Partition `jobs[0..n-1]` into `d` contiguous segments, minimize sum of per-segment max
    - dp(i, d) : min difficulty for `jobs[i..n-1]` with `d` days remaining
    - Sweep `j` from `i` to `n-d`, track **incremental max** as you go
    - Base: `d == 1` → max of all remaining jobs
- [https://leetcode.com/problems/painting-the-walls/](https://leetcode.com/problems/painting-the-walls/) — **Knapsack (transformed)**
    - Key transformation: paid painter taking wall `i` effectively covers `time[i]+1` walls total
    - Becomes: min cost knapsack where `sum(time[i]+1) >= n`
    - dp(i, rem) : min cost with `rem` walls remaining
    - Skip: `dp(i+1, rem)` | Take: `dp(i+1, max(0, rem-time[i]-1)) + cost[i]`
- [https://leetcode.com/problems/minimum-falling-path-sum-ii/](https://leetcode.com/problems/minimum-falling-path-sum-ii/) — **Grid DP with two-smallest optimization**
    - dp(i, j) : min path sum to reach (i, j)
    - Track two smallest **DP values** (not raw grid values) per row
    - If `j != s1_idx` use `s1`, else fall back to `s2`
    - Must compute two-smallest **after** row's DP values are ready — natural fit for **bottom-up**
- [https://leetcode.com/problems/string-compression-ii/](https://leetcode.com/problems/string-compression-ii/) — **Knapsack with 4D state**
    - dp(i, k, ch, len) : min encoded length for `s[i..n-1]`, `k` deletions left, last kept char `ch` with run length `len`
    - Delete: `dp(i+1, k-1, ch, len)` | Keep same: `dp(i+1, k, ch, len+1) + threshold_cost` | Keep diff: `dp(i+1, k, s[i], 1) + calcLen(prev_run)`
    - `threshold_cost` is 1 only at len = 1→2, 9→10, 99→100
    - Sentinel for ch must not collide with valid chars (use 26, not 0)
    - State space is sparse → use **top-down** with **hashmap** memo
- [https://leetcode.com/problems/arithmetic-slices-ii-subsequence/](https://leetcode.com/problems/arithmetic-slices-ii-subsequence/) — **Counting with map state**
    - Can't keep diff as array index (too large, can be negative) → `map<long long, int>` per index
    - For each pair (j, i): `diff = a[i]-a[j]`
    - `ans += dp[j][diff]` (extending existing APs → length ≥ 3, valid answer)
    - `dp[i][diff] += dp[j][diff] + 1` (the +1 is the new pair, just setup for future)
- [https://leetcode.com/problems/k-inverse-pairs-array/](https://leetcode.com/problems/k-inverse-pairs-array/) — **Counting with prefix sum optimization**
    - Insert element `i` into permutation of `1..i-1`. Placing at position from end creates 0 to `i-1` new inversions.
    - dp(i, k) = `sum(dp(i-1, k-j) for j in 0..i-1)` — sliding window sum
    - Optimize with prefix sum: `dp[i][k] = pre[k+1] - pre[max(0, k-i+1)]`
    - Handle negative modulo: `((a - b) % mod + mod) % mod`
    - Base: `dp[0][0] = 1`
- [https://leetcode.com/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/](https://leetcode.com/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/) — **Permutation DP**
    - Decide for the **shortest** stick (never blocks anything → cleanest subproblem)
    - dp(i, k) : # of ways to arrange i sticks with k visible
    - Place at start (visible): `dp(i-1, k-1)`
    - Place anywhere else (hidden): `dp(i-1, k) * (i-1)`
- [https://leetcode.com/problems/cherry-pickup-ii/](https://leetcode.com/problems/cherry-pickup-ii/) — **Grid DP, two agents**
    - dp(i, j1, j2) : max cherries from row i onward, robots at columns j1 and j2
    - If `j1 == j2`, count cell only once
    - 3 × 3 = 9 direction combinations per step — use nested loop `d1, d2 in {-1, 0, 1}`
    - Bounds check: skip if j1 or j2 go out of grid
- [https://leetcode.com/problems/count-vowels-permutation/](https://leetcode.com/problems/count-vowels-permutation/) — **Counting with transition table**
    - dp(i, ch) : # of strings of length i with last char ch
    - Store allowed transitions in a table, loop over `next[ch]` instead of if-else chains
    - Base: `i == n` → return 1
    - Answer: `sum(dp(1, ch))` for all 5 vowels
- [https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/](https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/) — **Grid/Counting**
    - dp(steps_remaining, position) : # of ways to return to 0
    - Cap position at `min(arrLen-1, steps/2)` — can't go further and return in time
- [https://leetcode.com/problems/minimum-cost-to-cut-a-stick/](https://leetcode.com/problems/minimum-cost-to-cut-a-stick/) — **Interval DP**
	- Sort cuts, add `0` and `n` as boundaries → states become indices into cuts array (≤102), not positions (≤10⁶)
	- dp(i, j) : min cost to handle all cuts between `cuts[i]` and `cuts[j]`
	- Try each split point `k` where `i < k < j`: `dp(i, k) + dp(k, j) + cuts[j] - cuts[i]`
	- Base: `j - i <= 1` → return 0 (no cuts between adjacent boundaries)
	- Loop must be `i+1 < k < j` (strictly between), otherwise infinite recursion
- [https://leetcode.com/problems/regular-expression-matching/](https://leetcode.com/problems/regular-expression-matching/) — **String Matching DP**
	- dp(i, j) : does `s[i..]` match `p[j..]`
	- Key: `*` pairs with its **preceding** character. Check `p[j+1] == '*'` to handle `p[j]p[j+1]` as a single unit.
	- If `p[j+1] == '*'`: use 0 copies → `dp(i, j+2)` | use 1+ copies (if `p[j]` matches `s[i]`) → `dp(i+1, j)` (stay at j to allow more copies)
	- Else: simple char match → `dp(i+1, j+1)`
	- Tricky base case: `i == n` doesn't mean success — remaining pattern must all be `x*` pairs to match empty string. Don't return early, let `*` logic skip pairs.
	- Guard `i < n` before accessing `s[i]` in both the `*` branch and the normal match branch.
- [https://leetcode.com/problems/palindrome-partitioning-iii/](https://leetcode.com/problems/palindrome-partitioning-iii/) — **Partition DP**
	- Split string into exactly `k` contiguous segments, minimize total changes to make each segment a palindrome
	- Precompute `cost[i][j]` = min changes to make `s[i..j]` a palindrome (two pointer, O(n²))
	- dp(i, k) : min cost for `s[i..n-1]` with `k` partitions remaining
	- Sweep end point `j` from `i` to `n-1`: `dp(j+1, k-1) + cost[i][j]`
	- No "skip" — every character must belong to a partition (this is partition DP, not knapsack)
	- Base: `i == n && k == 0` → return 0 | `i == n || k == 0` → return INF
- [https://leetcode.com/problems/find-the-sum-of-subsequence-powers/](https://leetcode.com/problems/find-the-sum-of-subsequence-powers/) — **Knapsack + Coordinate Compression**
	- Sort array. Min abs diff in any subsequence is always between **consecutive picks** in sorted order.
	- dp(i, k, d) : sum of powers of all subsequences ending at index `i`, `k` picks remaining, min diff so far is `d`
	- `d` can be up to 10⁹ — can't use as array index. But only C(50,2) = 1225 distinct pairwise diffs exist.
	- **Coordinate compress**: `dif[actual_diff] = compressed_id`, `rev[compressed_id] = actual_diff`. Don't forget to add INF sentinel.
	- Transition: for `j > i`: `new_min = min(rev[d], a[j]-a[i])`, recurse with `dif[new_min]`
	- Base: `k == 0` → return `rev[d] % mod` (actual diff value, the "power")
	- Answer: `sum over all i: recurse(i, k-1, dif[INF], a, rev)` — try each starting element
	- Decompress before comparing, recompress before recursing. Don't mix compressed ids with actual values.
- [https://leetcode.com/problems/number-of-ways-of-cutting-a-pizza/](https://leetcode.com/problems/number-of-ways-of-cutting-a-pizza/) — **Grid DP + 2D Prefix Sum**
	- After each cut, remaining pizza is always a **bottom-right subgrid** `[r..rows-1][c..cols-1]`
	- dp(r, c, k) : # of ways to cut remaining subgrid into `k` pieces
	- Horizontal cut at row `x`: give away `[r..x-1][c..cols-1]` → recurse `dp(x, c, k-1)`
	- Vertical cut at col `x`: give away `[r..rows-1][c..x-1]` → recurse `dp(r, x, k-1)`
	- Only valid if the given-away piece **has at least one apple** — use **2D prefix sum** for O(1) check
	- Base: `k == 1` → return 1 if remaining subgrid has apple, else 0
	- Don't use global row/col apple flags — they don't account for the current subgrid boundaries
- [https://leetcode.com/problems/super-egg-drop/](https://leetcode.com/problems/super-egg-drop/) — **DP + Binary Search Optimization**
    - dp(floors, eggs) = min moves to find critical floor in worst case
    - Try dropping from floor `x`: `1 + max(dp(x-1, k-1), dp(floors-x, k))` — max for worst case, min over all `x` for best strategy
    - Brute force over all `x` is O(floors²) — too slow
    - Key insight: as `x` increases, `dp(x-1, k-1)` increases and `dp(floors-x, k)` decreases → single crossover point → **binary search** for optimal `x`
    - Check both sides of crossover (integer boundary) — `best_i` and `best_i - 1`
    - Base: 0 floors → 0 moves | 1 egg → must go floor by floor → `floors` moves
    - Complexity: O(eggs × floors × log floors)
- [https://leetcode.com/problems/smallest-sufficient-team/](https://leetcode.com/problems/smallest-sufficient-team/) — **Bitmask DP**
    - Up to 16 required skills → encode as bitmask. Each bit = one skill covered.
    - dp[mask] = smallest team that covers `mask` skills (store actual vector of indices)
    - Bottom-up: for each mask, try adding each person → `dp[mask | person_mask]`
    - Can't precompute person masks if sticker-style (unlimited use + depends on current state), but CAN precompute for 0/1 knapsack style
    - Use `|` to add skills (not `^` which toggles)
    - Don't modify `dp[mask]` before copying — create new vector first
- [https://leetcode.com/problems/tallest-billboard/](https://leetcode.com/problems/tallest-billboard/) — **Knapsack with Diff Trick**
    - Split rods into two groups with equal sum, maximize that sum
    - Naive dp(i, sum1, sum2) is 20 × 5000 × 5000 — too large
    - Key: only track `diff = sum1 - sum2`. At end, need `diff == 0`
    - dp(i, diff) = max height of group1, using rods[0..i] where group1 - group2 = diff
    - Skip: `dp(i+1, diff)` | Group1: `dp(i+1, diff+a[i]) + a[i]` | Group2: `dp(i+1, diff-a[i])`
    - `+ a[i]` only for group1 — you're tracking group1's height, which only grows when adding to group1
    - Offset diff by 5000 for array index, or use hashmap
    - Answer: `dp(n, 0)`
- [https://leetcode.com/problems/russian-doll-envelopes/](https://leetcode.com/problems/russian-doll-envelopes/) — **Sort + LIS**
    - Sort by width ascending, **height descending** for same width
    - Descending height for same width prevents picking two same-width envelopes in LIS
    - Run O(n log n) LIS on heights → answer
    - Uses `lower_bound` for strictly increasing LIS
- [https://leetcode.com/problems/shortest-path-visiting-all-nodes/](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) — **BFS + Bitmask**
    - State: `(current_node, visited_mask)` — being at node X depends on which nodes you've collected so far
    - n ≤ 12 → mask fits in 2¹² = 4096, total states: 12 × 4096 ≈ 50K
    - Start BFS from **every node simultaneously**: push `(i, 1<<i)` for all `i`
    - Can **revisit nodes** — mask just tracks collection, not movement restriction
    - Stop when any state reaches `mask == (1<<n) - 1`
    - Use separate `visited[node][mask]` array — don't use 0 as both distance and "unvisited" sentinel
    - Check visited **before pushing** to queue, not after popping
    - **Pattern recognition:** "visit all nodes" + "shortest path" + small n (≤20) → BFS + bitmask (essentially BFS-based TSP)
- [https://leetcode.com/problems/can-i-win/](https://leetcode.com/problems/can-i-win/) — **Bitmask DP + Game Theory**
    - State: just `mask` (which numbers are picked). Turn and running total are derivable from mask.
    - Current player wins if any unpicked number `i` makes total ≥ target (immediate win), or if picking `i` leaves opponent in a losing state (`!dp(mask | (1<<i))`)
    - Early check: if `sum(1..n) < desiredTotal`, return false
    - 2²⁰ ≈ 1M states — comfortable
- [https://leetcode.com/problems/remove-boxes/](https://leetcode.com/problems/remove-boxes/) — **Interval DP with Extra State**
	- Score `k²` for removing `k` consecutive same-color boxes. Standard `dp(i,j)` fails because boxes outside the interval can combine with boxes inside.
	- dp(i, j, k) : max score from `a[i..j]` with `k` copies of `a[i]` attached from the left
	- Remove `a[i]` now: `dp(i+1, j, 0) + (k+1)²`
	- Find `a[m] == a[i]` in `[i+1..j]`, remove middle first, combine: `dp(i+1, m-1, 0) + dp(m, j, k+1)`
	- The `k+1` is the key — merging `a[i]` with a matching `a[m]` grows the chain for bigger score later
	- States: 100 × 100 × 100 = 1M — comfortable
	- **Pattern:** When standard interval DP fails, ask "is there info from OUTSIDE the interval that affects decisions INSIDE?" → that's the extra state.
- [https://leetcode.com/problems/make-array-strictly-increasing/](https://leetcode.com/problems/make-array-strictly-increasing/) — **DP + Binary Search + Map State**
	- dp(i, last_val) = min swaps to make `a[i..n-1]` increasing, given previous value was `last_val`
	- `last_val` can be huge → use `map<int, int>` per index or hashmap memo
	- Keep: if `a[i] > last_val`, recurse with `a[i]` as new `last_val`, no extra cost
	- Swap: binary search (`upper_bound`) in sorted `arr2` for smallest value > `last_val`, cost +1
	- Sort and deduplicate `arr2` first — duplicates are useless
	- Initial call: `recurse(0, -1)` — use -1 as sentinel so first element can always be kept
	- Watch: `upper_bound` on `arr2` must check `pos < b.size()`, not `pos < a.size()`
- [https://leetcode.com/problems/stone-game-viii/](https://leetcode.com/problems/stone-game-viii/) — **Game Theory Minimax + Prefix Sum Insight**
	- After any sequence of merges, the score is always a **prefix sum** of the original array — don't track modified array
	- dp(i) = max score diff for current player, choosing from `pre[i], pre[i+1], ..., pre[n-1]`
	- Take `pre[i]`: `pre[i] - dp(i+1)` | Skip: `dp(i+1)`
	- Must take at least 2 stones → start from `dp(2)`
	- Last prefix sum must be taken (can't skip everything) → `dp(n-1) = pre[n-1]`
	- O(n) bottom-up: `dp[i] = max(dp[i+1], pre[i] - dp[i+1])` right to left
- [https://leetcode.com/problems/number-of-ways-to-wear-different-hats-to-each-person/](https://leetcode.com/problems/number-of-ways-to-wear-different-hats-to-each-person/) — **Bitmask DP (flipped perspective)**
	- Hats up to 40 (too big for mask), people up to 10 (fits in 2¹⁰ = 1024)
	- Iterate over **hats**, bitmask on **people**: dp(hat, mask) = # of ways
	- Skip hat, or assign it to any person who likes it and isn't in mask yet
- [https://leetcode.com/problems/race-car/](https://leetcode.com/problems/race-car/) — **DP with Mathematical Insight**
	- After pressing A `n` times: position = `2^n - 1`, speed = `2^n`
	- dp(t) = min instructions to reach position `t` from 0 with speed 1
	- If `2^n - 1 == t` exactly → answer is `n`
	- **Overshoot:** smallest `n` where `2^n - 1 > t` → `n + 1 + dp((2^n-1) - t)`
	- **Undershoot:** `n-1` A's (reach `2^(n-1)-1 < t`), reverse, go back `m` A's, reverse again → `(n-1) + 1 + m + 1 + dp(t - (2^(n-1)-1) + (2^m-1))` for all `0 <= m < n-1`
	- Base: `dp(0) = 0`
	- Only two meaningful overshoot/undershoot points per target — keeps state space small
- [https://leetcode.com/problems/scramble-string/](https://leetcode.com/problems/scramble-string/) — **Interval DP on Two Strings**
	- Since both substrings always have equal length, use 3 states instead of 4: `dp(i1, i2, len)`
	- Split at length `k` (1 to len-1), two choices:
	- Keep order: `dp(i1, i2, k) & dp(i1+k, i2+k, len-k)`
	- Swap: `dp(i1, i2+len-k, k) & dp(i1+k, i2, len-k)` — right half of s2 starts at `i2+len-k`, not `i2+k`
	- Base: `len == 1` → `s1[i1] == s2[i2]`
	- Quick reject: if character frequencies of both substrings don't match, return false
	- States: 30 × 30 × 30 ≈ 27K — comfortable
- [https://leetcode.com/problems/find-the-shortest-superstring/](https://leetcode.com/problems/find-the-shortest-superstring/) — **Bitmask DP + TSP + Reconstruction**
	- Precompute `overlap[i][j]` = longest suffix of `a[i]` that matches prefix of `a[j]`
	- dp(mask, last) = min total length to cover strings in `mask`, ending with string `last`
	- Transition: for each unvisited `j`, cost to append = `len[j] - overlap[last][j]` (non-overlapping part)
	- Use `n` as sentinel for "no previous string" (arrays sized `[1<<n][n+1]`)
	- **Reconstruction:** track `par[mask][last] = next_best_j` only when `ans` improves (not every iteration)
	- Trace forward from `(0, n)` through `par`, appending `a[next].substr(overlap[last][next])` each step
	- n ≤ 12 → 2¹² × 12 ≈ 50K states, comfortable
- [https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/](https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/) — **DP + Binary Search + Pick Limit**
	- Like Job Scheduling but with at most `k` picks → add `k` as second state dimension
	- Sort events by start time, precompute `startTimes[]`
	- dp(i, k) = max value from events `[i..n-1]` with `k` picks remaining
	- Skip: `dp(i+1, k)` | Take: `dp(pos, k-1) + value[i]` where `pos = upper_bound(startTimes, a[i][1])` — first event starting AFTER end of event `i`
	- Base: `i == n` or `k == 0` → 0
	- Answer: `dp(0, k)` — NOT `dp(0, 0)` (that means 0 picks left, always 0)
- [https://leetcode.com/problems/largest-color-value-in-a-directed-graph/](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) — **DP on DAG with Topological Sort**
	- Topological sort (Kahn's BFS) gives a fixed processing order — all predecessors processed before successors
	- dp(node, color) = max count of `color` in any path ending at `node`
	- For each node `u` in topo order: propagate `dp[u][c]` to all successors `v`: `dp[v][c] = max(dp[v][c], dp[u][c])`
	- Then add 1 to `dp[u][color_of_u]` for u's own contribution
	- **Cycle detection:** if `processed_count != n` at end → cycle → return -1
	- Final answer: max over all `dp[node][color]`
- - https://leetcode.com/problems/swim-in-rising-water/description/
	- 2 ways - BS the answer, or Dijkstra
	- For this Dijkstra, you don't need a distance array, as we don't want to find distance to all nodes. We just want to find distance to the end node, which we are keeping as a state in priority queue.
- https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/
	- To count the # of ways that give the shortest path, just keep an additional ways array in Dijkstra. 
	- If we get a new shortest distance, we reset ways[v] = ways[u]
	- Else if we match the shortest distance, ways[v] += ways[u]

