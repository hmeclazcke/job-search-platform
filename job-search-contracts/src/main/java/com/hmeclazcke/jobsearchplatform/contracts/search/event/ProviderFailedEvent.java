package com.hmeclazcke.jobsearchplatform.contracts.search.event;

import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.provider.ProviderFailureType;

public record ProviderFailedEvent(
        String searchId,
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}