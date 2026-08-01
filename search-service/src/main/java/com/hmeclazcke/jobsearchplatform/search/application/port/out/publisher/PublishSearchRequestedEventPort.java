package com.hmeclazcke.jobsearchplatform.search.application.port.out.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.SearchRequestedEvent;

@FunctionalInterface
public interface PublishSearchRequestedEventPort {

    void publish(SearchRequestedEvent event);
}