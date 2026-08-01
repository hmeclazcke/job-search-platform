package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;

import java.util.Optional;

@FunctionalInterface
public interface LoadSearchStatePort {

    Optional<SearchState> load(String searchId);
}
