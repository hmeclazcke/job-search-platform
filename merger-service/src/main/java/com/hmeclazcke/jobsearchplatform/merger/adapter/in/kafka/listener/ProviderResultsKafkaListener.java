package com.hmeclazcke.jobsearchplatform.merger.adapter.in.kafka.listener;

import com.hmeclazcke.jobsearchplatform.contracts.ProviderResultsEvent;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.MergeProviderResultsCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.MergeProviderResultsUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka adapter that consumes successful provider result events.
 *
 * This listener consumes the provider.results.v1 topic. Its @KafkaListener references
 * KafkaConsumerConfig.providerResultsKafkaListenerContainerFactory(), which configures
 * Spring Kafka to deserialize the JSON message value as ProviderResultsEvent.
 */
@Component
public class ProviderResultsKafkaListener {

    private final MergeProviderResultsUseCase mergeProviderResultsUseCase;

    public ProviderResultsKafkaListener(MergeProviderResultsUseCase mergeProviderResultsUseCase) {
        this.mergeProviderResultsUseCase = mergeProviderResultsUseCase;
    }

    /**
     * The topic selects provider.results.v1, and the containerFactory selects the
     * deserializer defined in KafkaConsumerConfig.providerResultsKafkaListenerContainerFactory().
     */
    @KafkaListener(
            topics = "${app.kafka.topics.provider-results}",
            containerFactory = "providerResultsKafkaListenerContainerFactory"
    )
    public void listen(ProviderResultsEvent event) {
        MergeProviderResultsCommand command = new MergeProviderResultsCommand(
                event.searchId(),
                event.provider(),
                event.jobs()
        );

        mergeProviderResultsUseCase.merge(command);
    }
}