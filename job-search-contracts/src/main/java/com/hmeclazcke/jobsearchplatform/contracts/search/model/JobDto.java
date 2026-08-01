package com.hmeclazcke.jobsearchplatform.contracts.search.model;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;

public record JobDto(
        String title,
        String company,
        String location,
        String url,
        JobProvider source
) {
}