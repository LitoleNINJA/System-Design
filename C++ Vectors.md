### Sorting & Ordering

- `sort(v.begin(), v.end())` — ascending
- `sort(v.begin(), v.end(), greater<int>())` — descending
- `sort(v.begin(), v.end(), [](auto& a, auto& b){ return a.second < b.second; })`

- `partial_sort(v.begin(), v.begin()+k, v.end())` — only first k sorted

- `is_sorted(v.begin(), v.end())` — returns bool

- `reverse(v.begin(), v.end())
  
---
### Binary Search (sorted range required)

- `lower_bound(v.begin(), v.end(), x)` — first element **≥ x**

- `upper_bound(v.begin(), v.end(), x)` — first element **> x**

- `binary_search(v.begin(), v.end(), x)` — returns bool

- `equal_range(v.begin(), v.end(), x)` — pair of `{lower_bound, upper_bound}`

> [!warning] LIS Reminder

> Strictly increasing → `lower_bound` | Non-decreasing → `upper_bound`

---
### Removing & Deduplication

- **Erase-remove idiom:**
    - `v.erase(remove(v.begin(), v.end(), val), v.end())` — remove all val
    - `v.erase(remove_if(v.begin(), v.end(), pred), v.end())` — remove by condition

- **Deduplicate (sort first!):**
    - `sort(v.begin(), v.end())`
    - `v.erase(unique(v.begin(), v.end()), v.end())`

- **C++20 (cleaner):**
    - `erase(v, val)` — remove all val
    - `erase_if(v, pred)` — remove by condition

---
### Min / Max / Accumulate

- `*min_element(v.begin(), v.end())`
- `*max_element(v.begin(), v.end())`

- `auto [mn, mx] = minmax_element(v.begin(), v.end())` — C++17, pair of iterators

- `accumulate(v.begin(), v.end(), 0LL)` — sum (use `0LL` for long long!)

- `reduce(v.begin(), v.end(), 0LL)` — C++17, parallelizable accumulate

- `partial_sum(v.begin(), v.end(), pre.begin())` — prefix sum into `pre`

- `adjacent_difference(v.begin(), v.end(), d.begin())` — difference array

- `inner_product(v.begin(), v.end(), w.begin(), 0LL)` — dot product

---
### Counting & Finding
- `count(v.begin(), v.end(), val)` - Frequency
- `count_if(v.begin(), v.end(), pred)`

- `find(v.begin(), v.end(), val)` — iterator to first occurrence
- `find_if(v.begin(), v.end(), pred)`

---
### Transform & Generate

- `transform(v.begin(), v.end(), v.begin(), [](int x){ return x*2; })` — in-place

- `transform(a.begin(), a.end(), b.begin(), out.begin(), plus<>())` — element-wise add

- `fill(v.begin(), v.end(), 0)`

- `iota(v.begin(), v.end(), 0)` — fills with 0, 1, 2, 3, ...

- `generate(v.begin(), v.end(), [n=0]() mutable { return n++; })`

  

---

  

### Heap Operations

- `make_heap(v.begin(), v.end())` — max-heap

- `push_heap(v.begin(), v.end())` — after push_back, restore heap

- `pop_heap(v.begin(), v.end())` — move max to end, then pop_back

- `sort_heap(v.begin(), v.end())`

- `is_heap(v.begin(), v.end())`

  

---

  

### Partitioning

- `partition(v.begin(), v.end(), pred)` — satisfying elements moved to front

- `stable_partition(v.begin(), v.end(), pred)` — preserves relative order

- `partition_point(v.begin(), v.end(), pred)` — first non-matching (like binary search)

  

---

  

### C++20 Ranges

```cpp

#include <ranges>

namespace rv = std::ranges::views;

  

// pipeline syntax

v | rv::filter([](int x){ return x % 2 == 0; })

v | rv::transform([](int x){ return x * x; })

v | rv::take(5)

v | rv::drop(3)

v | rv::reverse

  

// ranges algorithms (no .begin()/.end() needed)

ranges::sort(v);

ranges::min(v);

ranges::max(v);

ranges::find(v, val);

ranges::count(v, val);

```

  

---

  

### C++23 Highlights

- `ranges::contains(v, val)` — replaces `find != end` pattern

- `ranges::fold_left(v, 0, plus<>())` — ranges version of accumulate

- `ranges::starts_with(v, prefix)`

- `ranges::ends_with(v, suffix)`

- `v | rv::chunk(3)` — split into groups of 3

- `v | rv::slide(3)` — sliding window of size 3

- `rv::zip(a, b)` — zip two ranges together

  

---

  

### Quick Init Patterns

- `vector<int> v(n, 0)` — n zeros

- `vector<int> v{1,2,3}` — initializer list

- `vector<vector<int>> g(n)` — adjacency list

- `auto [it_min, it_max] = minmax_element(v.begin(), v.end())` — structured bindings C++17