package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.merger.application.model.search.SearchState;

import java.util.Optional;

@FunctionalInterface
public interface LoadSearchStatePort {

    Optional<SearchState> load(String searchId);
}
