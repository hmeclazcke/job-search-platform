package com.hmeclazcke.jobsearchplatform.search.adapter.in.web;

import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.command.StartSearchCommand;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.query.GetSearchResultQuery;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.GetSearchResultResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.StartSearchResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.GetSearchResultUseCase;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.StartSearchUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(
        name = "Search",
        description = "Job search API."
)
@RestController
public class SearchController {

    private final StartSearchUseCase startSearchUseCase;
    private final GetSearchResultUseCase getSearchResultUseCase;

    public SearchController(StartSearchUseCase startSearchUseCase, GetSearchResultUseCase getSearchResultUseCase) {
        this.startSearchUseCase = startSearchUseCase;
        this.getSearchResultUseCase = getSearchResultUseCase;
    }


    @Operation(
            summary = "Start a job search",
            description = "Creates a search request and returns the id used to check its result later."
    )
    @ApiResponse(
            responseCode = "202",
            description = "Search request accepted",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StartSearchResponse.class),
                    examples = @ExampleObject("""
                        {
                          "searchId": "8a7a1f9a-2f7b-4a1e-9a9f-bf8c6d6d2f20"
                        }
                        """)
            )
    )
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


    @Operation(
            summary = "Get search result",
            description = "Returns the current search state, including partial results while providers are still processing."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search result found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchState.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Search not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "message": "Search not found",
                                      "searchId": "missing-search-id"
                                    }
                                    """)
                    )
            )
    })
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

    public record StartSearchRequest(
            @Schema(description = "Text to search in job titles or descriptions.", example = "java")
            String query,

            @Schema(description = "Preferred job location.", example = "remote")
            String location,

            @Schema(description = "Whether the search should prefer remote jobs.", example = "true")
            Boolean remote
    ) {
    }

    public record StartSearchResponse(String searchId) {
    }
}
