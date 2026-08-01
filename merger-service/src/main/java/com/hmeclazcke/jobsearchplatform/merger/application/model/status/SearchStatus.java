package com.hmeclazcke.jobsearchplatform.merger.application.model.status;

public enum SearchStatus {
    PENDING, // Waiting for one or more expected providers.
    COMPLETED, // All expected providers answered successfully.
    COMPLETED_WITH_FAILURES, // All expected providers answered, but at least one failed.
    FAILED // All expected providers failed.
}
