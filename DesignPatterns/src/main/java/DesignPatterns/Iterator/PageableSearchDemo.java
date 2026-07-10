package DesignPatterns.Iterator;

/**
 * Test driver / demo for the Pageable Search Results exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting class in this same
 * folder/package so this file compiles and runs:
 *
 *   1. SearchResults.java — implements Iterable<SearchResult>
 *                           ctor(SearchAPI api, String query)
 *                           iterator() returns a fresh Iterator that fetches
 *                           pages lazily as it advances.
 *
 * Until SearchResults exists you'll get "cannot find symbol" errors.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of Iterator (in Java specifically): the demo
 * uses `for (SearchResult r : results)` — pure standard Java syntax — and
 * yet pages are fetched lazily one at a time. The compiler is calling
 * results.iterator() once and then looping on hasNext()+next(). Your
 * implementation makes that work.
 *
 * The early-break test PROVES laziness: if we break after 3 results, only
 * page 0 should have been fetched. If you see [API] GET lines for pages 1
 * or 2 in the second block, your implementation is fetching eagerly.
 */
public class PageableSearchDemo {

    public static void main(String[] args) {
        SearchAPI api = new SearchAPI();

        System.out.println("=== Iterating ALL results with for-each ===");
        SearchResults results = new SearchResults(api, "java");
        int total = 0;
        for (SearchResult r : results) {
            System.out.println("Got: " + r);
            total++;
        }
        System.out.println("Total iterated: " + total);

        // Same Iterable, fresh iterator, early break — only page 0 should fetch.
        System.out.println("\n=== Iterating with early break (only first 3) ===");
        int count = 0;
        for (SearchResult r : new SearchResults(api, "java")) {
            System.out.println("Got: " + r);
            if (++count == 3) break;
        }
        System.out.println("Stopped after " + count);
        System.out.println("Pages fetched: only page 0 (proof of laziness — no [API] log for page 1 or 2)");
    }
}
