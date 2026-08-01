package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.kafka.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderResultsEvent;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.publisher.PublishProviderResultsPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProviderResultsPublisher implements PublishProviderResultsPort {

    private final KafkaTemplate<String, ProviderResultsEvent> kafkaTemplate;
    private final String topic;

    public KafkaProviderResultsPublisher(
            KafkaTemplate<String, ProviderResultsEvent> kafkaTemplate,
            @Value("${app.kafka.topics.provider-results}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(ProviderResultsEvent event) {
        kafkaTemplate.send(topic, event.searchId(), event);
    }
}