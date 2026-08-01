package com.hmeclazcke.jobsearchplatform.contracts.search.state;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.ProviderFailureType;

public record ProviderFailure(
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}
