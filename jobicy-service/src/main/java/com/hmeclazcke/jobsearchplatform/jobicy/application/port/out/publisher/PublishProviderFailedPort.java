package com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.ProviderFailedEvent;

@FunctionalInterface
public interface PublishProviderFailedPort {

    void publish(ProviderFailedEvent event);
}