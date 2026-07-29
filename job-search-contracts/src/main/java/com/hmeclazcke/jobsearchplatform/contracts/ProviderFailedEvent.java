package com.hmeclazcke.jobsearchplatform.contracts;

public record ProviderFailedEvent(
        String searchId,
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}