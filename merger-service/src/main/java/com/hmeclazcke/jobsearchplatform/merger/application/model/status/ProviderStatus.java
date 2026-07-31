package com.hmeclazcke.jobsearchplatform.merger.application.model.status;

public enum ProviderStatus {
    PENDING, // Waiting for this provider to answer.
    COMPLETED, // This provider answered successfully.
    FAILED // This provider answered with a controlled failure.
}