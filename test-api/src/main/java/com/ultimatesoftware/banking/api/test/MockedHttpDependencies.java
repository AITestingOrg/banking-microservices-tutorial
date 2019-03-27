package com.ultimatesoftware.banking.api.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public abstract class MockedHttpDependencies {
    private static final Logger LOG = LoggerFactory.getLogger(MockedHttpDependencies.class);
    protected WireMockServer personService;
    protected WireMockServer accountQueryService;
    protected WireMockServer accountCmdService;
    private final boolean enablePersonMock;
    private final boolean enableAccountQueryMock;
    private final boolean enableAccountCmdMock;

    public MockedHttpDependencies(boolean enablePersonMock, boolean enableAccountQueryMock, boolean enableAccountCmdMock) {
        this.enableAccountCmdMock = enableAccountCmdMock;
        this.enableAccountQueryMock = enableAccountQueryMock;
        this.enablePersonMock = enablePersonMock;
        if (enablePersonMock) {
            this.personService = createServer(8081, "wiremock/people");
        }
        if (enableAccountQueryMock) {
            this.accountQueryService = createServer(8084, "wiremock/accountquery");
        }
        if (enableAccountCmdMock) {
            this.accountCmdService = createServer(8082, "wiremock/accountcmd");
        }
    }

    public MockedHttpDependencies() {
        this.enableAccountCmdMock = true;
        this.enableAccountQueryMock = true;
        this.enablePersonMock = true;
    }

    @BeforeEach
    public void beforeEach() {
        if (enablePersonMock) {
            personService.start();
            personService.resetRequests();
        }
        if (enableAccountQueryMock) {
            accountQueryService.start();
            accountQueryService.resetRequests();
        }
        if (enableAccountCmdMock) {
            accountCmdService.start();
            accountCmdService.resetRequests();
        }
    }

    @AfterEach
    public void afterEach() {
        if (enablePersonMock) {
            personService.stop();
        }
        if (enableAccountCmdMock) {
            accountCmdService.stop();
        }
        if (enableAccountQueryMock) {
            accountQueryService.stop();
        }
    }

    private WireMockServer createServer(int port, String pathToMappings) {
        return new WireMockServer(options()
            .port(port)
            .usingFilesUnderClasspath(pathToMappings)
        );
    }
}
