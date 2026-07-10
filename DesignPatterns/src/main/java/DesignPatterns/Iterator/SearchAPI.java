package DesignPatterns.Iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * PROVIDED — DO NOT MODIFY.
 *
 * Stand-in for a paginated REST API. Holds 12 results internally and
 * returns them in pages of 5. Prints "[API] GET ..." every time fetchPage
 * is called so the demo can prove pages are fetched lazily.
 */
public class SearchAPI {

    private static final int PAGE_SIZE = 5;
    private final List<SearchResult> all;

    public SearchAPI() {
        all = new ArrayList<>();
        all.add(new SearchResult( 1, "Effective Java",                  0.95));
        all.add(new SearchResult( 2, "Java Concurrency in Practice",    0.92));
        all.add(new SearchResult( 3, "Java: The Complete Reference",    0.88));
        all.add(new SearchResult( 4, "Modern Java in Action",           0.86));
        all.add(new SearchResult( 5, "Head First Java",                 0.84));
        all.add(new SearchResult( 6, "Java Performance",                0.82));
        all.add(new SearchResult( 7, "Java Generics and Collections",   0.80));
        all.add(new SearchResult( 8, "Core Java Volume I",              0.78));
        all.add(new SearchResult( 9, "Core Java Volume II",             0.76));
        all.add(new SearchResult(10, "Java Puzzlers",                   0.74));
        all.add(new SearchResult(11, "Java in a Nutshell",              0.72));
        all.add(new SearchResult(12, "Spring in Action",                0.70));
    }

    public Page fetchPage(String query, int pageNumber) {
        System.out.printf("[API] GET /search?q=%s&page=%d%n", query, pageNumber);
        int from = pageNumber * PAGE_SIZE;
        if (from >= all.size()) {
            return new Page(List.of(), false, pageNumber);
        }
        int to = Math.min(from + PAGE_SIZE, all.size());
        List<SearchResult> items = new ArrayList<>(all.subList(from, to));
        boolean hasMore = to < all.size();
        return new Page(items, hasMore, pageNumber + 1);
    }
}
