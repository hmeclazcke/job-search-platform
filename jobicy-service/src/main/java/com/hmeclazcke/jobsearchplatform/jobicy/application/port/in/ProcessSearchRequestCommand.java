package com.hmeclazcke.jobsearchplatform.jobicy.application.port.in;

import com.hmeclazcke.jobsearchplatform.contracts.SearchCriteria;

public record ProcessSearchRequestCommand(
        String searchId,
        SearchCriteria criteria
) {
}