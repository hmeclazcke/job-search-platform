package com.hmeclazcke.jobsearchplatform.search.adapter.out.redis.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchStateKeys;
import com.hmeclazcke.jobsearchplatform.search.application.port.out.repository.LoadSearchStatePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RedisSearchStateRepository implements LoadSearchStatePort {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public RedisSearchStateRepository(
            StringRedisTemplate stringRedisTemplate,
            ObjectMapper objectMapper
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<SearchState> load(String searchId) {
        // Reads the JSON stored in Redis for this searchId, using the shared search state key format.
        String json = stringRedisTemplate.opsForValue().get(keyFor(searchId));

        if (json == null) {
            return Optional.empty();
        }

        try {
            // Redis returns the JSON string, which is converted back into the shared search state contract.
            return Optional.of(objectMapper.readValue(json, SearchState.class));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not deserialize search state from Redis", exception);
        }
    }

    private String keyFor(String searchId) {
        return SearchStateKeys.bySearchId(searchId);
    }
}