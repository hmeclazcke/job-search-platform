package com.hmeclazcke.jobsearchplatform.jobicy.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.jobicy.application.port.in.command.ProcessSearchRequestCommand;

@FunctionalInterface
public interface ProcessSearchRequestUseCase {

    void process(ProcessSearchRequestCommand command);
}