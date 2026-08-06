package com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.client;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.ProviderFailureType;
import com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.mapper.LinkedinJobMapper;
import com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.parser.LinkedinHtmlParser;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider.ProviderSearchException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class LinkedinClient {

    private static final Logger log = LoggerFactory.getLogger(LinkedinClient.class);
    private final RestClient restClient;
    private final LinkedinHtmlParser htmlParser;
    private final LinkedinJobMapper jobMapper;

    public LinkedinClient(
            RestClient.Builder builder,
            @Value("${app.linkedin.base-url}") String baseUrl,
            LinkedinHtmlParser htmlParser,
            LinkedinJobMapper jobMapper) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
        this.htmlParser = htmlParser;
        this.jobMapper = jobMapper;
    }

    @CircuitBreaker(name = "linkedin", fallbackMethod = "linkedinFallback")
    public List<JobDto> searchJobs(SearchCriteria criteria) {
        try {
            String html = restClient
                    .get()
                    .uri(uriBuilder -> {

                        uriBuilder.path("/jobs-guest/jobs/api/seeMoreJobPostings/search");

                        if (hasText(criteria.text())) {
                            uriBuilder.queryParam("keywords", criteria.text());
                        }

                        if (hasText(criteria.location())) {
                            uriBuilder.queryParam("location", criteria.location());
                        }

                        return uriBuilder.build();
                    })
                    .accept(MediaType.TEXT_HTML)
                    .retrieve()
                    .body(String.class);

            return htmlParser.parse(html)
                    .stream()
                    .map(jobMapper::toJobDto)
                    .toList();

        } catch (RestClientException exception) {
            throw new ProviderSearchException(
                    ProviderFailureType.UNAVAILABLE,
                    "LinkedIn provider is unavailable",
                    exception
            );
        }
    }

    // Resilience4j calls this fallback when searchJobs throws an exception
    // or when the circuit breaker is open and blocks the external call.
    private List<JobDto> linkedinFallback(SearchCriteria criteria, Exception exception) {
        // Keep provider failures that were already classified inside searchJobs.
        if (exception instanceof ProviderSearchException providerSearchException) {
            throw providerSearchException;
        }

        // This exception means the circuit breaker is open and blocked the call.
        if (exception instanceof CallNotPermittedException) {
            log.warn("LinkedIn circuit breaker is open. criteria={}", criteria);

            throw new ProviderSearchException(
                    ProviderFailureType.UNAVAILABLE,
                    "LinkedIn circuit breaker is open",
                    exception
            );
        }

        throw new ProviderSearchException(
                ProviderFailureType.UNAVAILABLE,
                "LinkedIn provider is unavailable",
                exception
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}