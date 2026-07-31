package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.kafka.publisher;

import com.hmeclazcke.jobsearchplatform.contracts.ProviderFailedEvent;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.out.publisher.PublishProviderFailedPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProviderFailedPublisher implements PublishProviderFailedPort {

    private final KafkaTemplate<String, ProviderFailedEvent> kafkaTemplate;
    private final String topic;

    public KafkaProviderFailedPublisher(
            KafkaTemplate<String, ProviderFailedEvent> kafkaTemplate,
            @Value("${app.kafka.topics.provider-failed}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(ProviderFailedEvent event) {
        kafkaTemplate.send(topic, event.searchId(), event);
    }
}