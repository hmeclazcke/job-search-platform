package com.hmeclazcke.jobsearchplatform.merger.application.port.in.command;

import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.provider.ProviderFailureType;

public record RegisterProviderFailureCommand(
        String searchId,
        JobProvider provider,
        ProviderFailureType failureType,
        String message
) {
}