package com.hmeclazcke.jobsearchplatform.search.adapter.in.web;

public record SearchErrorResponse(
        String message,
        String searchId
) {
}