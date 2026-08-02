package com.hmeclazcke.jobsearchplatform.linkedin.adapter.in.kafka.listener;

import com.hmeclazcke.jobsearchplatform.contracts.search.event.SearchRequestedEvent;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.command.ProcessSearchRequestCommand;
import com.hmeclazcke.jobsearchplatform.linkedin.application.port.in.usecase.ProcessSearchRequestUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SearchRequestedKafkaListener {

    private final ProcessSearchRequestUseCase processSearchRequestUseCase;

    public SearchRequestedKafkaListener(ProcessSearchRequestUseCase processSearchRequestUseCase) {
        this.processSearchRequestUseCase = processSearchRequestUseCase;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.search-requested}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(SearchRequestedEvent event) {

        ProcessSearchRequestCommand command = new ProcessSearchRequestCommand(
                event.searchId(),
                event.criteria()
        );

        // The use case has two valid outcomes: provider results or a provider failure event.
        // In both cases it returns normally, so Spring Kafka commits the offset for this consumer group.
        processSearchRequestUseCase.process(command);
    }
}