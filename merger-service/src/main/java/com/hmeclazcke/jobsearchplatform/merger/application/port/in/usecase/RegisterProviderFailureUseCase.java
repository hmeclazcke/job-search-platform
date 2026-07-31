package com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.RegisterProviderFailureCommand;

@FunctionalInterface
public interface RegisterProviderFailureUseCase {

    void register(RegisterProviderFailureCommand command);
}