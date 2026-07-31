package com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation;

import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.ProviderFailureType;

public record ProviderFailure(
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}