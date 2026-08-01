package com.hmeclazcke.jobsearchplatform.internaljobs.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.internaljobs.application.port.in.command.ProcessSearchRequestCommand;

@FunctionalInterface
public interface ProcessSearchRequestUseCase {

    void process(ProcessSearchRequestCommand command);
}