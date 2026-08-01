package com.hmeclazcke.jobsearchplatform.internaljobs.application.port.in.command;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;

public record ProcessSearchRequestCommand(
        String searchId,
        SearchCriteria criteria
) {
}