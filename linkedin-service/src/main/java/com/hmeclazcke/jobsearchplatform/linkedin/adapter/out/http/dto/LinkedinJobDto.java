package com.hmeclazcke.jobsearchplatform.linkedin.adapter.out.http.dto;

import java.time.LocalDate;

public record LinkedinJobDto(
        String id,
        String title,
        String company,
        String location,
        String url,
        LocalDate publishedDate
) {
}