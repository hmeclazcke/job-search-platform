package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.SearchAggregation;

@FunctionalInterface
public interface SaveSearchAggregationPort {

    void save(SearchAggregation aggregation);
}