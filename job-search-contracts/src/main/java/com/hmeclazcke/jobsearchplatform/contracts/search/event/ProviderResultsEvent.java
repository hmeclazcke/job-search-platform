package com.hmeclazcke.jobsearchplatform.contracts.search.event;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;

import java.util.List;

public record ProviderResultsEvent(
        String searchId,
        JobProvider provider,
        List<JobDto> jobs
) {
}