package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.config.LoadExpectedProvidersPort;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.LoadSearchStatePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchStateProvider {

    private final LoadSearchStatePort loadSearchStatePort;
    private final LoadExpectedProvidersPort loadExpectedProvidersPort;

    public SearchStateProvider(
            LoadSearchStatePort loadSearchStatePort,
            LoadExpectedProvidersPort loadExpectedProvidersPort
    ) {
        this.loadSearchStatePort = loadSearchStatePort;
        this.loadExpectedProvidersPort = loadExpectedProvidersPort;
    }

    public SearchState getCurrentState(String searchId) {

        // Loads the current state for this search, or creates the initial state
        // when this is the first provider response received for the searchId.
        return loadSearchStatePort.load(searchId)
                .orElseGet(() -> createInitialState(searchId));
    }

    private SearchState createInitialState(String searchId) {

        // Loads the providers required to complete a search.
        List<JobProvider> expectedProviders = loadExpectedProvidersPort.load();

        // Initial state example:
        // {
        //   "searchId": "abc-123",
        //   "status": "PENDING",
        //   "jobCount": 0,
        //   "jobs": [],
        //   "providers": {
        //     "JOBICY": "PENDING",
        //     "LINKEDIN": "PENDING"
        //   },
        //   "failures": [],
        //   "expectedProviders": ["JOBICY", "LINKEDIN"]
        // }
        return new SearchState(
                searchId,
                SearchStatus.PENDING,
                0,
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
