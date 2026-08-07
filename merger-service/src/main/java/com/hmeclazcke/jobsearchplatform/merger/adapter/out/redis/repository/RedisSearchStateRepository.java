package com.hmeclazcke.jobsearchplatform.merger.adapter.out.redis.repository;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchState;
import com.hmeclazcke.jobsearchplatform.contracts.search.state.SearchStateKeys;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.LoadSearchStatePort;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.repository.SaveSearchStatePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Redis adapter that stores the current search state as JSON.
 * <p>
 * The application layer uses LoadSearchStatePort and SaveSearchStatePort.
 * This adapter implements those ports with Redis as the external storage detail.
 */
@Repository
public class RedisSearchStateRepository implements LoadSearchStatePort, SaveSearchStatePort {

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
        // Reads the JSON stored in Redis for this searchId.
        String json = stringRedisTemplate.opsForValue().get(keyFor(searchId));

        if (json == null) {
            return Optional.empty();
        }

        try {
            // Redis returns JSON, so it is converted back into SearchState.
            return Optional.of(objectMapper.readValue(json, SearchState.class));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not deserialize search state from Redis", exception);
        }
    }

    @Override
    public void save(SearchState searchState) {
        try {
            // Redis stores strings here, so the SearchState is serialized to JSON before saving.
            String json = objectMapper.writeValueAsString(searchState);
            // Saves the JSON in Redis for this searchId.
            stringRedisTemplate.opsForValue().set(keyFor(searchState.searchId()), json);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize search state for Redis", exception);
        }
    }

    private String keyFor(String searchId) {
        return SearchStateKeys.bySearchId(searchId);
    }
}
