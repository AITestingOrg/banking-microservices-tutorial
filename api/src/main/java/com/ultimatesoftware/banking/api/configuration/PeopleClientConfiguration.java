package com.ultimatesoftware.banking.api.configuration;

import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;

import static com.ultimatesoftware.banking.api.configuration.PeopleClientConfiguration.PREFIX;

@ConfigurationProperties(PREFIX)
@BootstrapContextCompatible
public class PeopleClientConfiguration extends HttpClientConfiguration {
    static final String PREFIX = "people.client.gateway";

    private final PeopleClientConnectionPoolConfiguration connectionPoolConfiguration;

    private String url;

    public PeopleClientConfiguration() {
        this.connectionPoolConfiguration = new PeopleClientConnectionPoolConfiguration();
    }

    public PeopleClientConfiguration(ApplicationConfiguration applicationConfiguration) {
        super(applicationConfiguration);
        this.connectionPoolConfiguration = new PeopleClientConnectionPoolConfiguration();
    }

    public PeopleClientConfiguration(
        ApplicationConfiguration applicationConfiguration,
        PeopleClientConnectionPoolConfiguration connectionPoolConfiguration) {
        super(applicationConfiguration);
        this.connectionPoolConfiguration = connectionPoolConfiguration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public HttpClientConfiguration.ConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return connectionPoolConfiguration;
    }

    @ConfigurationProperties(HttpClientConfiguration.ConnectionPoolConfiguration.PREFIX)
    @BootstrapContextCompatible
    public static class PeopleClientConnectionPoolConfiguration extends HttpClientConfiguration.ConnectionPoolConfiguration {  }
}
