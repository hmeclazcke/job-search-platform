package com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository;

import com.hmeclazcke.jobsearchplatform.merger.application.model.aggregation.SearchAggregation;
import java.util.Optional;

@FunctionalInterface
public interface LoadSearchAggregationPort {

    Optional<SearchAggregation> load(String searchId);
}