package com.hmeclazcke.jobsearchplatform.merger.adapter.in.kafka.listener;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.ProviderFailedEvent;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.command.RegisterProviderFailureCommand;
import com.hmeclazcke.jobsearchplatform.merger.application.port.in.usecase.RegisterProviderFailureUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka adapter that consumes controlled provider failure events.
 *
 * This listener consumes the provider.failed.v1 topic. Its @KafkaListener references
 * KafkaConsumerConfig.providerFailedKafkaListenerContainerFactory(), which configures
 * Spring Kafka to deserialize the JSON message value as ProviderFailedEvent.
 */
@Component
public class ProviderFailedKafkaListener {

    private final RegisterProviderFailureUseCase registerProviderFailureUseCase;

    public ProviderFailedKafkaListener(RegisterProviderFailureUseCase registerProviderFailureUseCase) {
        this.registerProviderFailureUseCase = registerProviderFailureUseCase;
    }

    /**
     * The topic selects provider.failed.v1, and the containerFactory selects the
     * deserializer defined in KafkaConsumerConfig.providerFailedKafkaListenerContainerFactory().
     */
    @KafkaListener(
            topics = "${app.kafka.topics.provider-failed}",
            containerFactory = "providerFailedKafkaListenerContainerFactory"
    )
    public void listen(ProviderFailedEvent event) {
        RegisterProviderFailureCommand command = new RegisterProviderFailureCommand(
                event.searchId(),
                event.provider(),
                event.failureType(),
                event.message()
        );

        registerProviderFailureUseCase.register(command);
    }
}