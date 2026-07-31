package com.hmeclazcke.jobsearchplatform.merger.application.port.in.command;

import com.hmeclazcke.jobsearchplatform.contracts.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;

import java.util.List;

public record MergeProviderResultsCommand(
        String searchId,
        JobProvider provider,
        List<JobDto> jobs
) {
}