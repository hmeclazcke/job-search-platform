package com.hmeclazcke.jobsearchplatform.jobicy.application.port.in;

@FunctionalInterface
public interface ProcessSearchRequestUseCase {

    void process(ProcessSearchRequestCommand command);
}