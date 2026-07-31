package com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.ProviderResultsEvent;

@FunctionalInterface
public interface PublishProviderResultsPort {

    void publish(ProviderResultsEvent event);
}