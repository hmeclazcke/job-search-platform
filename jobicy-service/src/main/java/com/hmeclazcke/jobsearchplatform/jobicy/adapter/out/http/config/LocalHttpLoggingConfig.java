package com.hmeclazcke.jobsearchplatform.jobicy.adapter.out.http.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// This Spring Profile configuration is loaded only when
// the local-http-logging profile is active.
// It adds a RestClient interceptor that logs outgoing HTTP requests before they are executed.
@Configuration
@Profile("local-http-logging")
public class LocalHttpLoggingConfig {

    private static final Logger log = LoggerFactory.getLogger(LocalHttpLoggingConfig.class);

    @Bean
    RestClientCustomizer restClientLoggingCustomizer() {
        return builder -> builder.requestInterceptor((request, body, execution) -> {
            log.info("External HTTP request: method={}, uri={}",
                    request.getMethod(),
                    request.getURI());

            // Continue with the real HTTP request after logging it.
            return execution.execute(request, body);
        });
    }
}