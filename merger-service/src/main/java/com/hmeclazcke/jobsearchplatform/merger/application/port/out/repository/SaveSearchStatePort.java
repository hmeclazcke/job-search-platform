package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;

@FunctionalInterface
public interface SaveSearchStatePort {

    void save(SearchState searchState);
}
