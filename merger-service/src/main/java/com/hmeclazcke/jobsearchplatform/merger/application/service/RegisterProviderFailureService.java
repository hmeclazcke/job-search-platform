package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.ProviderFailure;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.RegisterProviderFailureCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.RegisterProviderFailureUseCase;
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
public class RegisterProviderFailureService implements RegisterProviderFailureUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RegisterProviderFailureService.class);

    private final SaveSearchStatePort saveSearchStatePort;
    private final SearchStateProvider searchStateProvider;
    private final SearchStatusCalculator searchStatusCalculator;

    public RegisterProviderFailureService(
            SaveSearchStatePort saveSearchStatePort,
            SearchStateProvider searchStateProvider,
            SearchStatusCalculator searchStatusCalculator
    ) {
        this.saveSearchStatePort = saveSearchStatePort;
        this.searchStateProvider = searchStateProvider;
        this.searchStatusCalculator = searchStatusCalculator;
    }

    @Override
    public void register(RegisterProviderFailureCommand command) {
        SearchState currentState = searchStateProvider.getCurrentState(command.searchId());

        ProviderFailure providerFailure = new ProviderFailure(
                command.provider(),
                command.failureType(),
                command.message()
        );

        // Keeps the failures already registered for this search and appends this provider failure.
        List<ProviderFailure> updatedFailures = new ArrayList<>(currentState.failures());
        updatedFailures.add(providerFailure);

        // Keeps the provider states already known for this search and marks this provider as failed.
        Map<JobProvider, ProviderStatus> updatedProviders = new HashMap<>(currentState.providers());
        updatedProviders.put(command.provider(), ProviderStatus.FAILED);

        // Recalculates the search status after this provider answered with a controlled failure.
        SearchStatus updatedStatus = searchStatusCalculator.calculate(
                currentState.expectedProviders(),
                updatedProviders,
                updatedFailures
        );

        // SearchState is immutable because it is a record.
        // To update the search state, a new instance is created with the updated failures, providers and status.
        SearchState updatedState = new SearchState(
                currentState.searchId(),
                updatedStatus,
                Instant.now(),
                currentState.jobCount(),
                currentState.jobs(),
                Map.copyOf(updatedProviders),
                List.copyOf(updatedFailures),
                currentState.expectedProviders()
        );

        saveSearchStatePort.save(updatedState);

        logger.info(
                "Registered provider failure: searchId={}, provider={}, failureType={}, status={}",
                command.searchId(),
                command.provider(),
                command.failureType(),
                updatedStatus
        );
    }
}
