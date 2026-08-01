package com.hmeclazcke.jobsearchplatform.merger.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.ProviderFailure;
import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.SearchAggregation;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.SearchAggregationStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.RegisterProviderFailureCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.RegisterProviderFailureUseCase;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.SaveSearchAggregationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterProviderFailureService implements RegisterProviderFailureUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RegisterProviderFailureService.class);

    private final SaveSearchAggregationPort saveSearchAggregationPort;
    private final SearchStateProvider searchStateProvider;
    private final SearchStatusCalculator searchStatusCalculator;

    public RegisterProviderFailureService(
            SaveSearchAggregationPort saveSearchAggregationPort,
            SearchStateProvider searchStateProvider,
            SearchStatusCalculator searchStatusCalculator
    ) {
        this.saveSearchAggregationPort = saveSearchAggregationPort;
        this.searchStateProvider = searchStateProvider;
        this.searchStatusCalculator = searchStatusCalculator;
    }

    @Override
    public void register(RegisterProviderFailureCommand command) {
        SearchAggregation currentSearch = searchStateProvider.getCurrentState(command.searchId());

        ProviderFailure providerFailure = new ProviderFailure(
                command.provider(),
                command.failureType(),
                command.message()
        );

        // Keeps the failures already registered for this search and appends this provider failure.
        List<ProviderFailure> updatedFailures = new ArrayList<>(currentSearch.failures());
        updatedFailures.add(providerFailure);

        // Keeps the provider states already known for this search and marks this provider as failed.
        Map<JobProvider, ProviderStatus> updatedProviders = new HashMap<>(currentSearch.providers());
        updatedProviders.put(command.provider(), ProviderStatus.FAILED);

        // Recalculates the search status after this provider answered with a controlled failure.
        SearchAggregationStatus updatedStatus = searchStatusCalculator.calculate(
                currentSearch.expectedProviders(),
                updatedProviders,
                updatedFailures
        );

        // SearchAggregation is immutable because it is a record.
        // To update the search state, a new instance is created with the updated failures, providers and status.
        SearchAggregation updatedSearch = new SearchAggregation(
                currentSearch.searchId(),
                updatedStatus,
                currentSearch.jobs(),
                Map.copyOf(updatedProviders),
                List.copyOf(updatedFailures),
                currentSearch.expectedProviders()
        );

        saveSearchAggregationPort.save(updatedSearch);

        logger.info(
                "Registered provider failure: searchId={}, provider={}, failureType={}, status={}",
                command.searchId(),
                command.provider(),
                command.failureType(),
                updatedStatus
        );
    }
}