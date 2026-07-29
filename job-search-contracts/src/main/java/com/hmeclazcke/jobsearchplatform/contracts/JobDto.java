package com.hmeclazcke.jobsearchplatform.contracts;

public record JobDto(
        String title,
        String company,
        String location,
        String url,
        JobProvider source
) {
}