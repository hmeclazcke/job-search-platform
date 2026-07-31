package com.hmeclazcke.jobsearchplatform.search.adapter.in.web;

import com.hmeclazcke.jobsearchplatform.search.application.port.in.command.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.StartSearchResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.StartSearchUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private final StartSearchUseCase startSearchUseCase;

    public SearchController(StartSearchUseCase startSearchUseCase) {
        this.startSearchUseCase = startSearchUseCase;
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StartSearchResponse startSearch(@RequestBody StartSearchRequest request) {

        // In Clean Architecture, the adapter receives an external request and translates it
        // into an application command. The use case receives the command, not the HTTP request.
        StartSearchResult result = startSearchUseCase.startSearch(
                new StartSearchCommand(
                        request.query(),
                        request.location(),
                        request.remote()
                )
        );

        return new StartSearchResponse(result.searchId());
    }

    public record StartSearchRequest(String query, String location, Boolean remote) {
    }

    public record StartSearchResponse(String searchId) {
    }
}