package com.hmeclazcke.jobsearchplatform.contracts.search.state;

public enum ProviderStatus {
    PENDING, // Waiting for this provider to answer.
    COMPLETED, // This provider answered successfully.
    FAILED // This provider answered with a controlled failure.
}