package com.hmeclazcke.jobsearchplatform.search.adapter.in.web;

import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.command.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.query.GetSearchResultQuery;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.GetSearchResultResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.StartSearchResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.GetSearchResultUseCase;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.StartSearchUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SearchController {

    private final StartSearchUseCase startSearchUseCase;
    private final GetSearchResultUseCase getSearchResultUseCase;

    public SearchController(StartSearchUseCase startSearchUseCase, GetSearchResultUseCase getSearchResultUseCase) {
        this.startSearchUseCase = startSearchUseCase;
        this.getSearchResultUseCase = getSearchResultUseCase;
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

    @GetMapping("/search/{searchId}")
    public ResponseEntity<?> getSearchResult(@PathVariable String searchId) {
        GetSearchResultQuery query = new GetSearchResultQuery(searchId);

        GetSearchResultResult result = getSearchResultUseCase.get(query);
        Optional<SearchState> searchState = result.searchState();

        if (searchState.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new SearchErrorResponse("Search not found", searchId));
        }

        return ResponseEntity.ok(searchState.get());
    }

    public record StartSearchRequest(String query, String location, Boolean remote) {
    }

    public record StartSearchResponse(String searchId) {
    }
}