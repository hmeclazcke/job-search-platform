package com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.MergeProviderResultsCommand;

@FunctionalInterface
public interface MergeProviderResultsUseCase {

    void merge(MergeProviderResultsCommand command);
}