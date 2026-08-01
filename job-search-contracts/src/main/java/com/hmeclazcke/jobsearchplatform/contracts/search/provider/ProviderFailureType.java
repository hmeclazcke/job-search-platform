package com.hmeclazcke.jobsearchplatform.contracts.search.provider;

public enum ProviderFailureType {
    TIMEOUT, // The provider did not respond within the expected time.
    RATE_LIMITED, // The provider rejected the request because too many requests were sent.
    UNAVAILABLE, // The provider could not be reached or is temporarily unavailable.
    INVALID_RESPONSE, // The provider returned a response that could not be processed.
    UNKNOWN // The provider failed for an unexpected or unclassified reason.
}