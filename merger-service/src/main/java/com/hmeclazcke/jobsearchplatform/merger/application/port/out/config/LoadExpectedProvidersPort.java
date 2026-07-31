package com.hmeclazcke.jobsearchplatform.merger.application.port.out.config;

import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import java.util.List;

@FunctionalInterface
public interface LoadExpectedProvidersPort {

    List<JobProvider> load();
}