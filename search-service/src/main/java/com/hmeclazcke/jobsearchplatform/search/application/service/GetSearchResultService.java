package com.hmeclazcke.jobsearchplatform.search.application.service;

import com.hmeclazcke.jobsearchplatform.search.application.port.in.query.GetSearchResultQuery;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.GetSearchResultResult;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase.GetSearchResultUseCase;
import com.hmeclazcke.jobsearchplatform.search.application.port.out.repository.LoadSearchStatePort;
import org.springframework.stereotype.Service;

@Service
public class GetSearchResultService implements GetSearchResultUseCase {

    private final LoadSearchStatePort loadSearchStatePort;

    public GetSearchResultService(LoadSearchStatePort loadSearchStatePort) {
        this.loadSearchStatePort = loadSearchStatePort;
    }

    @Override
    public GetSearchResultResult get(GetSearchResultQuery query) {
        return new GetSearchResultResult(
                loadSearchStatePort.load(query.searchId())
        );
    }
}