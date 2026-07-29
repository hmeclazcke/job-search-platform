package com.hmeclazcke.jobsearchplatform.contracts;

public record SearchCriteria(
        String text,
        String location,
        Boolean remote
) {
}