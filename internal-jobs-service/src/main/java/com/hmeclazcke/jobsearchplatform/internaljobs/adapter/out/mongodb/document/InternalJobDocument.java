package com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "internal_jobs")
public record InternalJobDocument(
        @Id
        String id,
        String title,
        String company,
        String location,
        String url,
        Boolean remote,
        String description,
        String department,
        Boolean active
) {
}