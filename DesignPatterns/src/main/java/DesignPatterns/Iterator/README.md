# Exercise — Pageable Search Results

> Iterator pattern · LLD practice
> The one pattern where the right answer is to use **Java's built-in interfaces** (`Iterable<T>` and `Iterator<T>`). Doing this exercise teaches both the pattern *and* how `for-each` works under the hood.

---

## What You'll Learn

This exercise unpacks something every Java developer uses daily but few have *built*:

```java
for (SearchResult r : results) { ... }   // ← what is actually happening here?
```

That for-each loop is syntactic sugar. The compiler rewrites it into:

```java
Iterator<SearchResult> it = results.iterator();   // 1. ask the Iterable for an Iterator
while (it.hasNext()) {                            // 2. while there's more...
    SearchResult r = it.next();                    // 3. ...get the next one
    /* loop body */
}
```

So `for-each` is a 3-step protocol over two interfaces:

| Interface | Purpose | Methods |
|---|---|---|
| `Iterable<T>` | "I'm something you can iterate over" | `Iterator<T> iterator()` |
| `Iterator<T>` | "I'm a stateful cursor over a sequence" | `boolean hasNext()`, `T next()` |

In other languages it has different names but the same shape — C++ has `begin()`/`end()` returning iterators with `++` and `*`; Python has `__iter__()`/`__next__()`. Same pattern, different syntax.

---

## Problem Statement

You are calling a search API that returns results in **pages** of 5. The total result set might be 12, 50, or 1000 items. You want application code to look like:

```java
SearchResults results = new SearchResults(api, "java");
for (SearchResult r : results) {
    System.out.println(r.title());
}
```

Two requirements pull in opposite directions:

1. **The caller must not see pagination.** No "give me page 0", no `while (page.hasMore)` loops sprinkled around. Just `for-each`.
2. **Pages must be fetched lazily.** If the caller `break`s after 3 results, only **page 0** should have been fetched — pages 1, 2, 3 are network round-trips you don't want to pay for unnecessarily.

Both requirements collapse into: **build a custom `Iterator<SearchResult>`** that internally manages page state and fetches the next page on demand.

---

## What's Already Provided

- **`SearchResult.java`** — record `(int id, String title, double score)`.
- **`Page.java`** — value object: `List<SearchResult> items`, `boolean hasMore`, `int nextPageNumber`.
- **`SearchAPI.java`** — fake backend with 12 hardcoded results, page size 5. `fetchPage(query, pageNumber)` prints `[API] GET ...` so the demo can prove pages were fetched lazily.
- **`PageableSearchDemo.java`** — test contract. Uses `for-each` and demonstrates early-break.

You **must not modify** the provided files — they represent the third-party API and the application's expectation of how iteration looks.

---

## Your Job

Implement **one** file:

**`SearchResults.java`** — the `Iterable<SearchResult>`:
```java
public class SearchResults implements Iterable<SearchResult> {
    private final SearchAPI api;
    private final String query;

    public SearchResults(SearchAPI api, String query) { ... }

    @Override
    public Iterator<SearchResult> iterator() {
        return new ResultsIterator();          // fresh iterator per call
    }

    // Inner class is the idiomatic way — has access to api/query directly.
    private class ResultsIterator implements Iterator<SearchResult> {
        private Page    currentPage;
        private int     indexInPage;

        public ResultsIterator() {
            this.currentPage = api.fetchPage(query, 0);
            this.indexInPage = 0;
        }

        @Override
        public boolean hasNext() {
            // 1. If we have items remaining on the current page → true.
            // 2. Else if the API said no more pages → false.
            // 3. Else fetch the next page lazily, then re-check.
        }

        @Override
        public SearchResult next() {
            if (!hasNext()) throw new NoSuchElementException();
            return currentPage.items().get(indexInPage++);
        }
    }
}
```

The `iterator()` method must return a **fresh** iterator on each call — calling `for-each` on the same `SearchResults` twice should restart from page 0. (Fresh iterator = correct semantics; cached iterator = subtle bug.)

---

## Class Hints

```
record SearchResult(int id, String title, double score) { }                           // PROVIDED
record Page(List<SearchResult> items, boolean hasMore, int nextPageNumber) { }        // PROVIDED
class  SearchAPI { Page fetchPage(String query, int pageNumber); }                    // PROVIDED

class SearchResults implements Iterable<SearchResult> {                               // YOUR JOB
    SearchAPI api;
    String    query;
    Iterator<SearchResult> iterator();    // returns a NEW iterator each call

    private class ResultsIterator implements Iterator<SearchResult> {
        Page  currentPage;
        int   indexInPage;
        boolean hasNext();
        SearchResult next();
    }
}
```

> 💡 **Structural fingerprint of Iterator:** the `Iterable` is the *factory*; the `Iterator` is the *stateful cursor*. State lives on the iterator (so two iterators over the same Iterable advance independently), never on the Iterable itself.

---

## Expected Output

```
=== Iterating ALL results with for-each ===
[API] GET /search?q=java&page=0
Got: SearchResult[id=1, title=Effective Java, score=0.95]
Got: SearchResult[id=2, title=Java Concurrency in Practice, score=0.92]
Got: SearchResult[id=3, title=Java: The Complete Reference, score=0.88]
Got: SearchResult[id=4, title=Modern Java in Action, score=0.86]
Got: SearchResult[id=5, title=Head First Java, score=0.84]
[API] GET /search?q=java&page=1
Got: SearchResult[id=6, title=Java Performance, score=0.82]
Got: SearchResult[id=7, title=Java Generics and Collections, score=0.80]
Got: SearchResult[id=8, title=Core Java Volume I, score=0.78]
Got: SearchResult[id=9, title=Core Java Volume II, score=0.76]
Got: SearchResult[id=10, title=Java Puzzlers, score=0.74]
[API] GET /search?q=java&page=2
Got: SearchResult[id=11, title=Java in a Nutshell, score=0.72]
Got: SearchResult[id=12, title=Spring in Action, score=0.70]
Total iterated: 12

=== Iterating with early break (only first 3) ===
[API] GET /search?q=java&page=0
Got: SearchResult[id=1, title=Effective Java, score=0.95]
Got: SearchResult[id=2, title=Java Concurrency in Practice, score=0.92]
Got: SearchResult[id=3, title=Java: The Complete Reference, score=0.88]
Stopped after 3
Pages fetched: only page 0 (proof of laziness — no [API] log for page 1 or 2)
```

**Three things to notice:**

1. **`[API] GET ...` lines appear *between* the `Got:` lines** — pages are fetched on demand, not upfront.
2. **The full iteration produces exactly 3 API calls** (pages 0, 1, 2) — one per page, and only as needed.
3. **The early-break case produces exactly 1 API call** — only page 0 was fetched. That's the laziness payoff. If you were paying per network call, this would be real money saved.

---

## What the Interviewer is Looking For

- **Implement Java's standard `Iterable<T>` and `Iterator<T>`** — don't invent your own. The whole point of the pattern *in Java* is to plug into the language's built-in iteration machinery.
- **State lives on the iterator, not the Iterable.** Calling `iterator()` twice gives two independent cursors. If state were on `SearchResults`, two callers iterating in parallel would corrupt each other.
- **Pages are fetched lazily.** Many candidates eagerly fetch all pages in the constructor — easier to write but defeats the whole point. Interviewers will probe this with: *"What if there are 10,000 pages?"*
- **`hasNext()` is responsible for advancing pages**, not `next()`. The convention: `hasNext()` does the "is there more, possibly fetching" check; `next()` just consumes the current item or throws.
- **`next()` should throw `NoSuchElementException` if called past the end** — this is the standard contract every Java collection iterator follows.

---

## Iterator vs Things It Gets Confused With

| vs | Distinction |
|---|---|
| **Iterator vs Visitor** | Iterator: caller pulls items one at a time. Visitor: structure pushes items at the visitor (visitor.visit(node) for each node). Iterator is *external iteration*; Visitor is *internal iteration*. Java's `forEach(Consumer)` is more like Visitor; `for-each` over Iterable is Iterator. |
| **Iterator vs Streams** | Java Streams are built *on top of* Iterators — internally a stream gets a `Spliterator` from an Iterable. Streams add lazy operations (filter/map) and parallelism. Use Iterator when you want simple per-element traversal; Stream when you want declarative pipelines. |
| **Iterator vs Generator (Python `yield`, JS `function*`)** | Generators are language-level Iterators with implicit state. Java doesn't have them — you write the state manually on the iterator object. Same semantics, more boilerplate. |

---

## How `for-each` Actually Works (the mechanics)

When the Java compiler sees:
```java
for (SearchResult r : results) { body }
```

It rewrites it (roughly) to:
```java
{
    Iterator<SearchResult> $it = results.iterator();
    while ($it.hasNext()) {
        SearchResult r = $it.next();
        body
    }
}
```

That's it. The compiler doesn't do anything magical — it just calls `iterator()` once, then loops on `hasNext()` + `next()`. **The pattern *is* what makes `for-each` work.** Once you've implemented `Iterable<T>` correctly, your custom type plugs straight into the language.

This is also why `Iterable<T>` is the **superinterface** of `Collection<T>`, `List<T>`, `Set<T>`, etc. — every collection in `java.util` is iterable because they all implement this one method.

---

## How to Attempt This Cold

Suggested order:

1. Read `SearchResult.java`, `Page.java`, `SearchAPI.java`. Note exactly what `fetchPage(query, pageNumber)` returns: a `Page` with items, hasMore flag, and the next page number.
2. Read `PageableSearchDemo.java`. Note that it constructs `new SearchResults(api, query)` and then uses `for-each` — that's all the API surface you need to implement.
3. Write `SearchResults.java`:
   - Constructor stashes `api` and `query`.
   - `iterator()` returns `new ResultsIterator()`.
4. Write the inner `ResultsIterator`:
   - Constructor: fetch page 0 immediately (so `hasNext` knows what to check first).
   - `hasNext`: the three-case logic (current page has items / no more pages / fetch next page and re-check).
   - `next`: defensive `if (!hasNext()) throw new NoSuchElementException();`, then return the next item and increment the in-page index.
5. Run the demo. Verify the `[API]` lines appear at the right moments.

Two pitfalls to watch:

- **Eager fetch in the constructor** — fetching all pages in `new SearchResults(api, query)` is the most common wrong answer. It defeats laziness and breaks the early-break test.
- **`hasNext()` that has side effects called twice in a row** — the standard contract is `hasNext()` is *idempotent*. Calling it twice without an intervening `next()` should not skip items, and should not double-fetch. The structure I sketched above handles this correctly because `hasNext()` only fetches when `indexInPage >= items.size()`, which the *first* call resets to 0 of the new page; the second call sees `indexInPage (0) < items.size`, no fetch.

---

## Files in this Exercise

| File | Status | Role |
|------|--------|------|
| `SearchResult.java`         | ✅ provided | Record — single result item |
| `Page.java`                 | ✅ provided | Record — one page from the API |
| `SearchAPI.java`            | ✅ provided | Fake backend, 12 results in 3 pages |
| `PageableSearchDemo.java`   | ✅ provided | Client / `main` — uses `for-each` |
| `SearchResults.java`        | 🛠 your job | The `Iterable<SearchResult>` + inner `Iterator<SearchResult>` |
