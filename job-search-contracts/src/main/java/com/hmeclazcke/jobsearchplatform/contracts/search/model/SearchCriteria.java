package com.hmeclazcke.jobsearchplatform.contracts.search.model;

public record SearchCriteria(
        String text,
        String location,
        Boolean remote
) {
}