package com.hmeclazcke.jobsearchplatform.search.application.port.in;

public record StartSearchCommand(
        String query,
        String location,
        Boolean remote
) {
}