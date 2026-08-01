package com.hmeclazcke.jobsearchplatform.internaljobs.application.port.out.provider;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;

import java.util.List;

@FunctionalInterface
public interface SearchInternalJobsPort {

    List<JobDto> searchJobs(SearchCriteria criteria);
}