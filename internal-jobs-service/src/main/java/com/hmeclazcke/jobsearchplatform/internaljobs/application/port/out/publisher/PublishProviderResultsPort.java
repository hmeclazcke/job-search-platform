package com.hmeclazcke.jobsearchplatform.internaljobs.application.port.out.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderResultsEvent;

@FunctionalInterface
public interface PublishProviderResultsPort {

    void publish(ProviderResultsEvent event);
}