package com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.cache;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.client.LinkedinClient;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider.SearchJobsPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Component
public class CachedLinkedinJobsAdapter implements SearchJobsPort {

    private static final Logger log = LoggerFactory.getLogger(CachedLinkedinJobsAdapter.class);

    private final LinkedinClient linkedinClient;

    public CachedLinkedinJobsAdapter(LinkedinClient linkedinClient) {
        this.linkedinClient = linkedinClient;
    }

    // This adapter decorates the real LinkedIn client with Redis-backed cache behavior.
    // The key attribute uses Spring Expression Language.
    // #criteria is the method argument, and #root.target.cacheKey(...) calls the method below.
    @Override
    @Cacheable(
            cacheNames = "linkedin-jobs",
            key = "#root.target.cacheKey(#criteria)",
            unless = "#result.isEmpty()"
    )
    public List<JobDto> searchJobs(SearchCriteria criteria) {
        log.info("LinkedIn cache miss: fetching jobs from LinkedIn. criteria={}", criteria);

        return linkedinClient.searchJobs(criteria);
    }

    // Builds the Redis cache key for this LinkedIn search.
    // If another search uses the same criteria, it can reuse the cached results.
    public String cacheKey(SearchCriteria criteria) {
        return normalize(criteria.text()) + ":"
                + normalize(criteria.location()) + ":"
                + Boolean.TRUE.equals(criteria.remote());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}