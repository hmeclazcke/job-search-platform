package com.hmeclazcke.jobsearchplatform.search.adapter.out.kafka.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.SearchRequestedEvent;
import com.hmeclazcke.jobsearchplatform.search.application.port.out.publisher.PublishSearchRequestedEventPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchRequestedEventPublisher implements PublishSearchRequestedEventPort {

    private final KafkaTemplate<String, SearchRequestedEvent> kafkaTemplate;
    private final String searchRequestedTopic;

    public KafkaSearchRequestedEventPublisher(
            KafkaTemplate<String, SearchRequestedEvent> kafkaTemplate,
            @Value("${app.kafka.topics.search-requested}") String searchRequestedTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.searchRequestedTopic = searchRequestedTopic;
    }

    @Override
    public void publish(SearchRequestedEvent event) {
        kafkaTemplate.send(searchRequestedTopic, event.searchId(), event);
    }
}
