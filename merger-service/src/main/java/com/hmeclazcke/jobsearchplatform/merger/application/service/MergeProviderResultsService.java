package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.MergeProviderResultsCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.MergeProviderResultsUseCase;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.SaveSearchStatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MergeProviderResultsService implements MergeProviderResultsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(MergeProviderResultsService.class);

    private final SaveSearchStatePort saveSearchStatePort;
    private final SearchStateProvider searchStateProvider;
    private final SearchStatusCalculator searchStatusCalculator;

    public MergeProviderResultsService(
            SaveSearchStatePort saveSearchStatePort,
            SearchStateProvider searchStateProvider,
            SearchStatusCalculator searchStatusCalculator
    ) {
        this.saveSearchStatePort = saveSearchStatePort;
        this.searchStateProvider = searchStateProvider;
        this.searchStatusCalculator = searchStatusCalculator;
    }

    @Override
    public void merge(MergeProviderResultsCommand command) {
        SearchState currentState = searchStateProvider.getCurrentState(command.searchId());

        // Keeps the jobs already collected for this search and appends the jobs received from this provider.
        List<JobDto> updatedJobs = new ArrayList<>(currentState.jobs());
        updatedJobs.addAll(command.jobs());

        // Keeps the provider states already known for this search and marks this provider as completed.
        Map<JobProvider, ProviderStatus> updatedProviders = new HashMap<>(currentState.providers());
        updatedProviders.put(command.provider(), ProviderStatus.COMPLETED);

        // Recalculates the search status after this provider answered successfully.
        SearchStatus updatedStatus = searchStatusCalculator.calculate(
                currentState.expectedProviders(),
                updatedProviders,
                currentState.failures()
        );

        // SearchState is immutable because it is a record.
        // To update the search state, a new instance is created with the updated jobs, providers and status.
        SearchState updatedState = new SearchState(
                currentState.searchId(),
                updatedStatus,
                Instant.now(),
                updatedJobs.size(),
                List.copyOf(updatedJobs),
                Map.copyOf(updatedProviders),
                currentState.failures(),
                currentState.expectedProviders()
        );

        saveSearchStatePort.save(updatedState);

        logger.info(
                "Merged provider results: searchId={}, provider={}, jobsCount={}, status={}",
                command.searchId(),
                command.provider(),
                command.jobs().size(),
                updatedStatus
        );
    }
}
