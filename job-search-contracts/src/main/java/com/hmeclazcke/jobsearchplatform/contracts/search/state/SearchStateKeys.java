package com.hmeclazcke.jobsearchplatform.contracts.search.state;

public final class SearchStateKeys {

    // Redis is a key/value store, so each search state is stored under a namespaced key:
    // search:{searchId}
    private static final String KEY_PREFIX = "search:";

    private SearchStateKeys() {
    }

    // Builds the Redis key used to store one SearchState.
    // Example: searchId "abc-123" becomes "search:abc-123".
    public static String bySearchId(String searchId) {
        return KEY_PREFIX + searchId;
    }
}