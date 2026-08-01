package com.hmeclazcke.jobsearchplatform.search.application.port.in.result;

import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;

import java.util.Optional;

public record GetSearchResultResult(
        Optional<SearchState> searchState
) {
}