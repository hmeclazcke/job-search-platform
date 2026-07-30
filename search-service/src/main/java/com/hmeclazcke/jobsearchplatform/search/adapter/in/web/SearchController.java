package com.hmeclazcke.jobsearchplatform.search.adapter.in.web;

import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.StartSearchUseCase;
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