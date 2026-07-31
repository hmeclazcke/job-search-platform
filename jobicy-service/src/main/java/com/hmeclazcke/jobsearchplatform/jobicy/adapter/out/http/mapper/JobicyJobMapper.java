package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.mapper;

import com.hmeclazcke.jobsearchplatform.contracts.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.dto.JobicyJobDto;
import org.springframework.stereotype.Component;

@Component
public class JobicyJobMapper {

    public JobDto toJobDto(JobicyJobDto jobicyJob) {
        return new JobDto(
                jobicyJob.jobTitle(),
                jobicyJob.companyName(),
                jobicyJob.jobGeo(),
                jobicyJob.url(),
                JobProvider.JOBICY
        );
    }
}