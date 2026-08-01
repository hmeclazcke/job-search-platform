package com.hmeclazcke.jobsearchplatform.contracts.search.state;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;

import java.util.List;
import java.util.Map;

/**
 * Represents the current state of a search after provider responses are processed.
 *
 * The state can remain PENDING while some providers have not answered yet,
 * or it can become final when every expected provider answered successfully or failed.
 *
 * Example:
 * {
 *   "searchId": "abc-123",
 *   "status": "COMPLETED_WITH_FAILURES",
 *   "jobs": [
 *     {
 *       "title": "Java Developer",
 *       "company": "Initech",
 *       "location": "Remote",
 *       "url": "https://example.com/jobs/1",
 *       "source": "JOBICY"
 *     }
 *   ],
 *   "providers": {
 *     "JOBICY": "COMPLETED",
 *     "LINKEDIN": "FAILED"
 *   },
 *   "failures": [
 *     {
 *       "provider": "LINKEDIN",
 *       "failureType": "UNAVAILABLE",
 *       "message": "LinkedIn provider is unavailable"
 *     }
 *   ],
 *   "expectedProviders": ["JOBICY", "LINKEDIN"]
 * }
 */
public record SearchState(
        String searchId,
        SearchStatus status,
        List<JobDto> jobs,
        Map<JobProvider, ProviderStatus> providers,
        List<ProviderFailure> failures,
        List<JobProvider> expectedProviders
) {
}
