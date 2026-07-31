package com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.search.application.port.in.command.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.StartSearchResult;

@FunctionalInterface
public interface StartSearchUseCase {

    StartSearchResult startSearch(StartSearchCommand command);
}