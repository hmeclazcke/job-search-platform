package com.hmeclazcke.jobsearchplatform.contracts;

public record SearchRequestedEvent(
        String searchId,
        SearchCriteria criteria
) {
}