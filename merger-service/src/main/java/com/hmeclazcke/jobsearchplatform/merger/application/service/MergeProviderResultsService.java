package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.SearchAggregation;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.SearchAggregationStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.MergeProviderResultsCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.MergeProviderResultsUseCase;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.SaveSearchAggregationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MergeProviderResultsService implements MergeProviderResultsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(MergeProviderResultsService.class);

    private final SaveSearchAggregationPort saveSearchAggregationPort;
    private final SearchStateProvider searchStateProvider;
    private final SearchStatusCalculator searchStatusCalculator;

    public MergeProviderResultsService(
            SaveSearchAggregationPort saveSearchAggregationPort,
            SearchStateProvider searchStateProvider,
            SearchStatusCalculator searchStatusCalculator
    ) {
        this.saveSearchAggregationPort = saveSearchAggregationPort;
        this.searchStateProvider = searchStateProvider;
        this.searchStatusCalculator = searchStatusCalculator;
    }

    @Override
    public void merge(MergeProviderResultsCommand command) {
        SearchAggregation currentSearch = searchStateProvider.getCurrentState(command.searchId());

        // Keeps the jobs already collected for this search and appends the jobs received from this provider.
        List<JobDto> updatedJobs = new ArrayList<>(currentSearch.jobs());
        updatedJobs.addAll(command.jobs());

        // Keeps the provider states already known for this search and marks this provider as completed.
        Map<JobProvider, ProviderStatus> updatedProviders = new HashMap<>(currentSearch.providers());
        updatedProviders.put(command.provider(), ProviderStatus.COMPLETED);

        // Recalculates the search status after this provider answered successfully.
        SearchAggregationStatus updatedStatus = searchStatusCalculator.calculate(
                currentSearch.expectedProviders(),
                updatedProviders,
                currentSearch.failures()
        );

        // SearchAggregation is immutable because it is a record.
        // To update the search state, a new instance is created with the updated jobs, providers and status.
        SearchAggregation updatedSearch = new SearchAggregation(
                currentSearch.searchId(),
                updatedStatus,
                List.copyOf(updatedJobs),
                Map.copyOf(updatedProviders),
                currentSearch.failures(),
                currentSearch.expectedProviders()
        );

        saveSearchAggregationPort.save(updatedSearch);

        logger.info(
                "Merged provider results: searchId={}, provider={}, jobsCount={}, status={}",
                command.searchId(),
                command.provider(),
                command.jobs().size(),
                updatedStatus
        );
    }
}