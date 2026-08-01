package com.hmeclazcke.jobsearchplatform.merger.adapter.in.kafka.config;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderFailedEvent;
import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderResultsEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 * Kafka consumer configuration for the merger service.
 *
 * This service consumes two different Kafka event types: provider results and provider failures.
 * Since producers do not send Spring type headers, Spring Kafka cannot infer the Java class
 * from the message itself. Each listener must explicitly choose the container factory that
 * knows how to deserialize its topic payload.
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;


    /**
     * Creates the listener container factory used by ProviderResultsKafkaListener.java
     *
     * ProviderResultsKafkaListener binds the provider.results.v1 topic to this factory with:
     * containerFactory = "providerResultsKafkaListenerContainerFactory".
     * That binding is what makes messages from that topic deserialize as ProviderResultsEvent.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProviderResultsEvent> providerResultsKafkaListenerContainerFactory() {
        JsonDeserializer<ProviderResultsEvent> valueDeserializer = new JsonDeserializer<>(ProviderResultsEvent.class);
        valueDeserializer.addTrustedPackages("com.hmeclazcke.jobsearchplatform.contracts");
        valueDeserializer.setUseTypeHeaders(false);

        DefaultKafkaConsumerFactory<String, ProviderResultsEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerProperties(),
                        new StringDeserializer(),
                        valueDeserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, ProviderResultsEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    /**
     * Creates the listener container factory used by ProviderFailedKafkaListener.java
     *
     * ProviderFailedKafkaListener binds the provider.failed.v1 topic to this factory with:
     * containerFactory = "providerFailedKafkaListenerContainerFactory".
     * That binding is what makes messages from that topic deserialize as ProviderFailedEvent.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProviderFailedEvent> providerFailedKafkaListenerContainerFactory() {
        JsonDeserializer<ProviderFailedEvent> valueDeserializer = new JsonDeserializer<>(ProviderFailedEvent.class);
        valueDeserializer.addTrustedPackages("com.hmeclazcke.jobsearchplatform.contracts");
        valueDeserializer.setUseTypeHeaders(false);

        DefaultKafkaConsumerFactory<String, ProviderFailedEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerProperties(),
                        new StringDeserializer(),
                        valueDeserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, ProviderFailedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    /**
     * Builds the Kafka consumer properties for the manually created consumer factories.
     *
     * The values are still defined in application.yml and injected into this class with @Value.
     * Because we create DefaultKafkaConsumerFactory instances ourselves, those values must be
     * passed explicitly to each factory instead of relying only on Spring Boot auto-configuration.
     */
    private Map<String, Object> consumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset
        );
    }
}