package com.ultimatesoftware.banking.transactions.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public abstract class MockedHttpDependencies {
    private static final Logger LOG = LoggerFactory.getLogger(MockedHttpDependencies.class);
    protected final WireMockServer customersService;
    protected final WireMockServer accountQueryService;
    protected final WireMockServer accountCmdService;

    public MockedHttpDependencies() {
        this.customersService = createServer(8085, "/wiremock/customers/mappings");
        this.accountQueryService = createServer(8084, "/wiremock/accountquery/mappings");
        this.accountCmdService = createServer(8082, "/wiremock/accountcmd/mappings");;
    }

    @BeforeEach
    public void beforeEach() {
        customersService.start();
        accountQueryService.start();
        accountCmdService.start();
        customersService.resetRequests();
        accountCmdService.resetRequests();
        accountQueryService.resetRequests();
    }

    @AfterEach
    public void afterEach() {
        customersService.stop();
        accountCmdService.stop();
        accountQueryService.stop();
    }

    private WireMockServer createServer(int port, String pathToMappings) {
        return new WireMockServer(options()
            .port(port)
            .usingFilesUnderDirectory(pathToMappings)
        );
    }
}
