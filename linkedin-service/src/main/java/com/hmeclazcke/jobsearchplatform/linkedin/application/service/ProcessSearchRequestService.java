package com.hmeclazcke.jobsearchplatform.linkedin.application.service;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderFailedEvent;
import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderResultsEvent;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.command.ProcessSearchRequestCommand;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.usecase.ProcessSearchRequestUseCase;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider.ProviderSearchException;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider.SearchJobsPort;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.publisher.PublishProviderFailedPort;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.publisher.PublishProviderResultsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessSearchRequestService implements ProcessSearchRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessSearchRequestService.class);

    private final SearchJobsPort searchJobsPort;
    private final PublishProviderResultsPort publishProviderResultsPort;
    private final PublishProviderFailedPort publishProviderFailedPort;

    public ProcessSearchRequestService(
            SearchJobsPort searchJobsPort,
            PublishProviderResultsPort publishProviderResultsPort,
            PublishProviderFailedPort publishProviderFailedPort) {
        this.searchJobsPort = searchJobsPort;
        this.publishProviderResultsPort = publishProviderResultsPort;
        this.publishProviderFailedPort = publishProviderFailedPort;
    }

    @Override
    public void process(ProcessSearchRequestCommand command) {
        try {
            List<JobDto> jobs = searchJobsPort.searchJobs(command.criteria());

            ProviderResultsEvent event = new ProviderResultsEvent(
                    command.searchId(),
                    JobProvider.LINKEDIN,
                    jobs
            );

            publishProviderResultsPort.publish(event);

            log.info("Published LinkedIn results: searchId={}, jobsCount={}",
                    command.searchId(),
                    jobs.size());
        } catch (ProviderSearchException exception) {

            // A provider failure is a valid processing outcome,
            // so it is published instead of rethrown.
            ProviderFailedEvent event = new ProviderFailedEvent(
                    command.searchId(),
                    JobProvider.LINKEDIN,
                    exception.failureType(),
                    exception.getMessage()
            );

            publishProviderFailedPort.publish(event);

            log.warn("Published LinkedIn failure: searchId={}, failureType={}, message={}",
                    command.searchId(),
                    exception.failureType(),
                    exception.getMessage());
        }
    }
}