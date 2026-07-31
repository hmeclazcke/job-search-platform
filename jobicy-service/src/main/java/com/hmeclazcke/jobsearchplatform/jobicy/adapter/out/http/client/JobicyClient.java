package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.client;

import com.hmeclazcke.jobsearchplatform.contracts.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.dto.JobicyResponse;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.mapper.JobicyJobMapper;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.provider.SearchJobsPort;
import com.hmeclazcke.jobsearchplatform.contracts.ProviderFailureType;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.provider.ProviderSearchException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Component
public class JobicyClient implements SearchJobsPort {

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

    @Override
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

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}