package DesignPatterns.Iterator;

import java.util.List;

/**
 * PROVIDED — DO NOT MODIFY. One page returned by the SearchAPI.
 *
 *   items          — the result rows on this page
 *   hasMore        — true if more pages exist beyond this one
 *   nextPageNumber — what to pass back to fetchPage() to get the next page
 */
public record Page(List<SearchResult> items, boolean hasMore, int nextPageNumber) { }
