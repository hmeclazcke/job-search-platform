package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.merger.application.model.search.SearchState;

@FunctionalInterface
public interface SaveSearchStatePort {

    void save(SearchState searchState);
}
