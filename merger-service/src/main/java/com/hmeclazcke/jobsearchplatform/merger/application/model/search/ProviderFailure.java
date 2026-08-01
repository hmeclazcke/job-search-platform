package com.hmeclazcke.jobsearchplatform.merger.application.model.search;

import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.provider.ProviderFailureType;

public record ProviderFailure(
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}
