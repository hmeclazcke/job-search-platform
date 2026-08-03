package com.hmeclazcke.jobsearchplatform.contracts.search.model;

import com.hmeclazcke.jobsearchplatform.contracts.search.provider.JobProvider;

import java.io.Serializable;

public record JobDto(
        String title,
        String company,
        String location,
        String url,
        JobProvider source
) implements Serializable { // Required because Spring Cache stores this DTO in Redis using Java serialization.
}