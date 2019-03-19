package com.ultimatesoftware.banking.transactions.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;

public class HttpMock {
    private int port;
    protected WireMockServer wireMockServer;

    public HttpMock(int port) {
        this.port = port;
    }

    public void start() {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
    }

    public void stop() {
        wireMockServer.stop();
    }
}
