package com.hmeclazcke.jobsearchplatform.contracts;

import java.util.List;

public record ProviderResultsEvent(
        String searchId,
        JobProvider provider,
        List<JobDto> jobs
) {
}