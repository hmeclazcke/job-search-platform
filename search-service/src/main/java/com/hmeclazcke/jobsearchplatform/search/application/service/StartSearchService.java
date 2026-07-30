package com.hmeclazcke.jobsearchplatform.search.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.contracts.SearchRequestedEvent;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchUseCase;
import com.hmeclazcke.jobsearchplatform.search.application.port.out.PublishSearchRequestedEventPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartSearchService implements StartSearchUseCase {

    private final PublishSearchRequestedEventPort publishSearchRequestedEventPort;

    public StartSearchService(PublishSearchRequestedEventPort publishSearchRequestedEventPort) {
        this.publishSearchRequestedEventPort = publishSearchRequestedEventPort;
    }

    @Override
    public StartSearchResult startSearch(StartSearchCommand command) {
        String searchId = UUID.randomUUID().toString();

        SearchCriteria criteria = new SearchCriteria(
                command.query(),
                command.location(),
                command.remote()
        );

        SearchRequestedEvent event = new SearchRequestedEvent(
                searchId,
                criteria
        );

        publishSearchRequestedEventPort.publish(event);

        return new StartSearchResult(searchId);
    }
}