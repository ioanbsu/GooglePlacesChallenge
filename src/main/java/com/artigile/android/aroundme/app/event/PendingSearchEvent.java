package com.artigile.android.aroundme.app.event;

/**
 * User: ioanbsu
 * Date: 11/28/12
 * Time: 5:06 PM
 */
public class PendingSearchEvent {

    private String searchQuery;

    public PendingSearchEvent(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
