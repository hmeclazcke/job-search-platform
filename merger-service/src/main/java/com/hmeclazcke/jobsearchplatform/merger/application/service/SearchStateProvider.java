package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.SearchAggregation;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.SearchAggregationStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.config.LoadExpectedProvidersPort;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.LoadSearchAggregationPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchStateProvider {

    private final LoadSearchAggregationPort loadSearchAggregationPort;
    private final LoadExpectedProvidersPort loadExpectedProvidersPort;

    public SearchStateProvider(
            LoadSearchAggregationPort loadSearchAggregationPort,
            LoadExpectedProvidersPort loadExpectedProvidersPort
    ) {
        this.loadSearchAggregationPort = loadSearchAggregationPort;
        this.loadExpectedProvidersPort = loadExpectedProvidersPort;
    }

    public SearchAggregation getCurrentState(String searchId) {

        // Loads the current state for this search, or creates the initial state
        // when this is the first provider response received for the searchId.
        return loadSearchAggregationPort.load(searchId)
                .orElseGet(() -> createInitialState(searchId));
    }

    private SearchAggregation createInitialState(String searchId) {

        // Loads the providers required to complete a search.
        List<JobProvider> expectedProviders = loadExpectedProvidersPort.load();

        // Initial state example:
        // {
        //   "searchId": "abc-123",
        //   "status": "PENDING",
        //   "jobs": [],
        //   "providers": {
        //     "JOBICY": "PENDING",
        //     "LINKEDIN": "PENDING"
        //   },
        //   "failures": [],
        //   "expectedProviders": ["JOBICY", "LINKEDIN"]
        // }
        return new SearchAggregation(
                searchId,
                SearchAggregationStatus.PENDING,
                List.of(),
                expectedProviders.stream()
                        .collect(Collectors.toMap(
                                provider -> provider,
                                provider -> ProviderStatus.PENDING
                        )),
                List.of(),
                expectedProviders
        );
    }
}