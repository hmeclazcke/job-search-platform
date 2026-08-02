package com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.command.ProcessSearchRequestCommand;

@FunctionalInterface
public interface ProcessSearchRequestUseCase {

    void process(ProcessSearchRequestCommand command);
}