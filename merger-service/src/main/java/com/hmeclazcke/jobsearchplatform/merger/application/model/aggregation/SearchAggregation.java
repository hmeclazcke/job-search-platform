package com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation;

import com.hmeclazcke.jobsearchplatform.contracts.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.ProviderStatus;
import com.hmeclazcke.jobsearchplatform.merger.application.model.status.SearchAggregationStatus;

import java.util.List;
import java.util.Map;

/**
 * Represents the current aggregated state of a search after provider responses are processed.
 *
 * The state can still be PENDING while some providers have not answered yet,
 * or it can be final when every expected provider answered successfully or failed.
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
public record SearchAggregation(
        String searchId,
        SearchAggregationStatus status,
        List<JobDto> jobs,
        Map<JobProvider, ProviderStatus> providers,
        List<ProviderFailure> failures,
        List<JobProvider> expectedProviders
) {
}