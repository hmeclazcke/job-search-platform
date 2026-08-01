package com.hmeclazcke.jobsearchplatform.merger.application.port.in.command;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;

import java.util.List;

public record MergeProviderResultsCommand(
        String searchId,
        JobProvider provider,
        List<JobDto> jobs
) {
}