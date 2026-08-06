package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.client;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.ProviderFailureType;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.dto.JobicyResponse;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.mapper.JobicyJobMapper;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.provider.ProviderSearchException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Objects;

@Component
public class JobicyClient {

    private static final Logger log = LoggerFactory.getLogger(JobicyClient.class);
    private final RestClient restClient;
    private final JobicyJobMapper mapper;

    public JobicyClient(
            RestClient.Builder restClientBuilder,
            @Value("${app.jobicy.base-url}") String baseUrl,
            JobicyJobMapper mapper
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.mapper = mapper;
    }

    @CircuitBreaker(name = "jobicy", fallbackMethod = "jobicyFallback")
    public List<JobDto> searchJobs(SearchCriteria criteria) {
        if (Boolean.FALSE.equals(criteria.remote())) {
            return List.of();
        }

        try {
            JobicyResponse response = restClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/api/v2/remote-jobs");

                        if (hasText(criteria.text())) {
                            uriBuilder.queryParam("tag", criteria.text());
                        }

                        return uriBuilder.build();
                    })
                    .retrieve()
                    .body(JobicyResponse.class);

            if (response == null || response.jobs() == null) {
                return List.of();
            }

            return response.jobs()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(mapper::toJobDto)
                    .toList();
        } catch (HttpClientErrorException.TooManyRequests exception) {
            throw new ProviderSearchException(
                    ProviderFailureType.RATE_LIMITED,
                    "Jobicy rate limit exceeded",
                    exception
            );
        } catch (RestClientException exception) {
            throw new ProviderSearchException(
                    ProviderFailureType.UNAVAILABLE,
                    "Jobicy API is unavailable",
                    exception
            );
        }
    }

    // Resilience4j calls this fallback when searchJobs throws an exception
    // or when the circuit breaker is open and blocks the external call.
    private List<JobDto> jobicyFallback(SearchCriteria criteria, Exception exception) {
        // Keep provider failures that were already classified inside searchJobs.
        if (exception instanceof ProviderSearchException providerSearchException) {
            throw providerSearchException;
        }

        // This exception means the circuit breaker is open and blocked the call.
        if (exception instanceof CallNotPermittedException) {
            log.warn("Jobicy circuit breaker is open. criteria={}", criteria);

            throw new ProviderSearchException(
                    ProviderFailureType.UNAVAILABLE,
                    "Jobicy circuit breaker is open",
                    exception
            );
        }

        throw new ProviderSearchException(
                ProviderFailureType.UNAVAILABLE,
                "Jobicy API is unavailable",
                exception
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}