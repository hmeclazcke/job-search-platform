package com.hmeclazcke.jobsearchplatform.jobicy.adapter.in.kafka;

import com.hmeclazcke.jobsearchplatform.contracts.SearchRequestedEvent;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.in.ProcessSearchRequestCommand;
import com.hmeclazcke.jobsearchplatform.jobicy.application.port.in.ProcessSearchRequestUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SearchRequestedKafkaListener {

    private final ProcessSearchRequestUseCase processSearchRequestUseCase;

    public SearchRequestedKafkaListener(ProcessSearchRequestUseCase processSearchRequestUseCase) {
        this.processSearchRequestUseCase = processSearchRequestUseCase;
    }

    @KafkaListener(topics = "${app.kafka.topics.search-requested}")
    public void listen(SearchRequestedEvent event) {
        ProcessSearchRequestCommand command = new ProcessSearchRequestCommand(
                event.searchId(),
                event.criteria()
        );

        processSearchRequestUseCase.process(command);
    }
}