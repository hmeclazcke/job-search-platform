package com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.repository;

import com.hmeclazcke.jobsearchplatform.internaljobs.adapter.out.mongodb.document.InternalJobDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB creates the runtime implementation for this repository.
 *
 * The adapter depends on this interface to read internal job documents from MongoDB,
 * without writing the query boilerplate manually.
 */
public interface InternalJobMongoRepository extends MongoRepository<InternalJobDocument, String> {

    List<InternalJobDocument> findByActiveTrue();
}