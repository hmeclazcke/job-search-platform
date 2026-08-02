package com.hmeclazcke.jobsearchplatform.linkedin.application.port.out.provider;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.model.SearchCriteria;

import java.util.List;

@FunctionalInterface
public interface SearchJobsPort {

    List<JobDto> searchJobs(SearchCriteria criteria);
}