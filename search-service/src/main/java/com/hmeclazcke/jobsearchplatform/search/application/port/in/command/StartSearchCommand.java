package com.hmeclazcke.jobsearchplatform.search.application.port.in.command;

public record StartSearchCommand(
        String query,
        String location,
        Boolean remote
) {
}