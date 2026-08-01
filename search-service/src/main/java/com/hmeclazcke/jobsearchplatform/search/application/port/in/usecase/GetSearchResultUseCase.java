package com.hmeclazcke.jobsearchplatform.search.application.port.in.usecase;

import com.hmeclazcke.jobsearchplatform.search.application.port.in.query.GetSearchResultQuery;
import com.hmeclazcke.jobsearchplatform.search.application.port.in.result.GetSearchResultResult;

@FunctionalInterface
public interface GetSearchResultUseCase {

    GetSearchResultResult get(GetSearchResultQuery query);
}