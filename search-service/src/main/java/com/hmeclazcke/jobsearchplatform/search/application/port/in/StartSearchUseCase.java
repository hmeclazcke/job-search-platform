package com.hmeclazcke.jobsearchplatform.search.application.port.in;

@FunctionalInterface
public interface StartSearchUseCase {

    StartSearchResult startSearch(StartSearchCommand command);
}