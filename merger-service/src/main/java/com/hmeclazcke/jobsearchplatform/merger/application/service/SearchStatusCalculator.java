package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.model.search.ProviderFailure;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.SearchStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SearchStatusCalculator {

    /**
     * Calculates the overall search status from the provider states and registered failures.
     *
     * Decision order:
     * 1. If at least one expected provider is still pending, the search remains PENDING.
     * 2. If every expected provider failed, the search is FAILED.
     * 3. If every expected provider answered and at least one failed, the search is COMPLETED_WITH_FAILURES.
     * 4. Otherwise, every expected provider answered successfully, so the search is COMPLETED.
     */
    public SearchStatus calculate(
            List<JobProvider> expectedProviders,
            Map<JobProvider, ProviderStatus> providers,
            List<ProviderFailure> failures
    ) {
        boolean hasPendingProvider = expectedProviders.stream()
                .anyMatch(provider -> providers.get(provider) == ProviderStatus.PENDING);

        if (hasPendingProvider) {
            return SearchStatus.PENDING;
        }

        boolean allProvidersFailed = expectedProviders.stream()
                .allMatch(provider -> providers.get(provider) == ProviderStatus.FAILED);

        if (allProvidersFailed) {
            return SearchStatus.FAILED;
        }

        if (!failures.isEmpty()) {
            return SearchStatus.COMPLETED_WITH_FAILURES;
        }

        return SearchStatus.COMPLETED;
    }
}
