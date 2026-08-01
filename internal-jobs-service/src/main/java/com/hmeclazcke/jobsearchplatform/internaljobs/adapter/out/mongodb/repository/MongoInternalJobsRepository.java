package com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.repository;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.ProviderFailureType;
import com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.document.InternalJobDocument;
import com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.mapper.InternalJobMapper;
import com.hmeclazcke.jobsearchplatform.internaljobs.application.port.out.provider.ProviderSearchException;
import com.hmeclazcke.jobsearchplatform.internaljobs.application.port.out.provider.SearchInternalJobsPort;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoInternalJobsRepository implements SearchInternalJobsPort {

    private final InternalJobMongoRepository internalJobMongoRepository;
    private final InternalJobMapper internalJobMapper;

    public MongoInternalJobsRepository(
            InternalJobMongoRepository internalJobMongoRepository,
            InternalJobMapper internalJobMapper
    ) {
        this.internalJobMongoRepository = internalJobMongoRepository;
        this.internalJobMapper = internalJobMapper;
    }

    @Override
    public List<JobDto> searchJobs(SearchCriteria criteria) {
        try {
            return internalJobMongoRepository.findByActiveTrue()
                    .stream()
                    .filter(job -> matchesCriteria(job, criteria))
                    .map(internalJobMapper::toJobDto)
                    .toList();
        } catch (DataAccessException exception) {
            throw new ProviderSearchException(
                    ProviderFailureType.UNAVAILABLE,
                    "Internal jobs provider is unavailable",
                    exception
            );
        }
    }

    private boolean matchesCriteria(InternalJobDocument job, SearchCriteria criteria) {
        // Only criteria provided by the request are applied.
        return matchesText(job, criteria.text())
                && matchesLocation(job, criteria.location())
                && matchesRemote(job, criteria.remote());
    }

    private boolean matchesText(InternalJobDocument job, String text) {
        if (text == null || text.isBlank()) {
            return true;
        }

        String normalizedText = text.toLowerCase();

        // The text criterion matches if it appears in any searchable field.
        return containsIgnoreCase(job.title(), normalizedText)
                || containsIgnoreCase(job.company(), normalizedText)
                || containsIgnoreCase(job.description(), normalizedText)
                || containsIgnoreCase(job.department(), normalizedText);
    }

    private boolean matchesLocation(InternalJobDocument job, String location) {
        if (location == null || location.isBlank()) {
            return true;
        }

        return containsIgnoreCase(job.location(), location.toLowerCase());
    }

    private boolean matchesRemote(InternalJobDocument job, Boolean remote) {
        if (remote == null) {
            return true;
        }

        return remote.equals(job.remote());
    }

    private boolean containsIgnoreCase(String value, String normalizedText) {
        return value != null && value.toLowerCase().contains(normalizedText);
    }
}