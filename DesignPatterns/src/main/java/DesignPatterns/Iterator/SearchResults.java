package DesignPatterns.Iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SearchResults implements Iterable<SearchResult> {
    private final SearchAPI searchAPI;
    private final String query;

    public SearchResults(SearchAPI searchAPI, String query) {
        this.searchAPI = searchAPI;
        this.query = query;
    }

    @Override
    public Iterator<SearchResult> iterator() {
        return new ResultsIterator();
    }

    private class ResultsIterator implements Iterator<SearchResult> {
        private Page currentPage;
        private int pageIndex;

        public ResultsIterator() {
            this.currentPage = searchAPI.fetchPage(query, 0);
            this.pageIndex = 0;
        }

        @Override
        public boolean hasNext() {
            if(this.pageIndex < this.currentPage.items().size()) {
                return true;
            }
            if(this.currentPage.hasMore()) {
                this.currentPage = searchAPI.fetchPage(query, this.currentPage.nextPageNumber());
                this.pageIndex = 0;
                return true;
            }
            return false;
        }

        @Override
        public SearchResult next() {
            if(!this.hasNext()) {
                throw new NoSuchElementException();
            }

            return this.currentPage.items().get(pageIndex++);
        }
    }
}
