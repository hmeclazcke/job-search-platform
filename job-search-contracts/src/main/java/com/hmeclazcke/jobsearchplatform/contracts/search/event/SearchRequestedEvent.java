package com.hmeclazcke.jobsearchplatform.contracts.search.event;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;

public record SearchRequestedEvent(
        String searchId,
        SearchCriteria criteria
) {
}