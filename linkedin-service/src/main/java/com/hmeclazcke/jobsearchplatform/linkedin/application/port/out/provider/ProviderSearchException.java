package com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.ProviderFailureType;

public class ProviderSearchException extends RuntimeException {

    private final ProviderFailureType failureType;

    public ProviderSearchException(ProviderFailureType failureType, String message, Throwable cause) {
        super(message, cause);
        this.failureType = failureType;
    }

    public ProviderFailureType failureType() {
        return failureType;
    }
}