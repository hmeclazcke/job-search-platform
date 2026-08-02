package com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.command;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;

public record ProcessSearchRequestCommand(
        String searchId,
        SearchCriteria criteria
) {
}