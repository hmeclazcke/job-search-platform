package com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.provider;

import com.hmeclazcke.jobsearchplatform.contracts.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.SearchCriteria;

import java.util.List;

@FunctionalInterface
public interface SearchJobsPort {

    List<JobDto> searchJobs(SearchCriteria criteria);
}