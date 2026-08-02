package com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.mapper;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.dto.LinkedinJobDto;
import org.springframework.stereotype.Component;

@Component
public class LinkedinJobMapper {

    public JobDto toJobDto(LinkedinJobDto dto) {
        return new JobDto(
                dto.title(),
                dto.company(),
                dto.location(),
                dto.url(),
                JobProvider.LINKEDIN
        );
    }
}