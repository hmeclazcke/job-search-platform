package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JobicyResponse(
        List<JobicyJobDto> jobs
) {
}