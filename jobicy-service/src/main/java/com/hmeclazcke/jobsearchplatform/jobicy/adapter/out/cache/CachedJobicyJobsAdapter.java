package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.cache;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.client.JobicyClient;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.provider.SearchJobsPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Component
public class CachedJobicyJobsAdapter implements SearchJobsPort {

    private static final Logger log = LoggerFactory.getLogger(CachedJobicyJobsAdapter.class);

    private final JobicyClient jobicyClient;

    public CachedJobicyJobsAdapter(JobicyClient jobicyClient) {
        this.jobicyClient = jobicyClient;
    }

    // This adapter decorates the real Jobicy client with Redis-backed cache behavior.
    // The key attribute uses Spring Expression Language.
    // #criteria is the method argument, and #root.target.cacheKey(...) calls the method below.
    @Override
    @Cacheable(
            cacheNames = "jobicy-jobs",
            key = "#root.target.cacheKey(#criteria)",
            unless = "#result.isEmpty()"
    )
    public List<JobDto> searchJobs(SearchCriteria criteria) {
        log.info("Jobicy cache miss: fetching jobs from Jobicy. criteria={}", criteria);

        return jobicyClient.searchJobs(criteria);
    }

    // Builds the Redis cache key for this Jobicy search.
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