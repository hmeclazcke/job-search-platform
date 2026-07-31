package com.hmeclazcke.jobsearchplatform.merger.adapter.out.config;

import com.hmeclazcke.jobsearchplatform.contracts.JobProvider;
import com.hmeclazcke.jobsearchplatform.merger.application.port.out.config.LoadExpectedProvidersPort;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads the expected providers from application.yml.
 *
 * The application layer depends on LoadExpectedProvidersPort, while this adapter
 * translates the external Spring configuration into that application port.
 */
@Component
@ConfigurationProperties(prefix = "app.providers")
public class ExpectedProvidersProperties implements LoadExpectedProvidersPort {

    private List<JobProvider> expected;

    @Override
    public List<JobProvider> load() {
        return expected;
    }

    public void setExpected(List<JobProvider> expected) {
        this.expected = expected;
    }
}