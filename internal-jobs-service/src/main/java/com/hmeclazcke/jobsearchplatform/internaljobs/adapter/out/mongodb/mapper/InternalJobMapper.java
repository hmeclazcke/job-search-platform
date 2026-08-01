package com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.mapper;

import com.hmeclazcke.jobsearchplatform.contracts.search.model.JobDto;
import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;
import com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.document.InternalJobDocument;
import org.springframework.stereotype.Component;

@Component
public class InternalJobMapper {

    public JobDto toJobDto(InternalJobDocument document) {
        return new JobDto(
                document.title(),
                document.company(),
                document.location(),
                document.url(),
                JobProvider.INTERNAL
        );
    }
}