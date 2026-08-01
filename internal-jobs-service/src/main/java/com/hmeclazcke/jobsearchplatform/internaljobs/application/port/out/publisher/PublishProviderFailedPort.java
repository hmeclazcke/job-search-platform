package com.hmeclazcke.jobsearchplatform.internaljobs.application.port.out.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderFailedEvent;

@FunctionalInterface
public interface PublishProviderFailedPort {

    void publish(ProviderFailedEvent event);
}