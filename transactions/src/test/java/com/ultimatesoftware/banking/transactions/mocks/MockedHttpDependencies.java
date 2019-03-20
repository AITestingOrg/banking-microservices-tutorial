package com.ultimatesoftware.banking.transactions.mocks;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class MockedHttpDependencies {
    private static final Logger LOG = LoggerFactory.getLogger(MockedHttpDependencies.class);
    protected static String customersName = "customers";
    protected static String accountsQueryName = "account-query";
    protected static String accountsCmdName = "account-cmd";
    protected static String consulHost = "127.0.0.1";
    protected static String consulPort = "8500";

    @AfterEach
    public void afterEach() throws IOException {
        // clear mock request journal
        HttpClient.getBuilder()
            .setPath("/__admin/requests")
            .setPort(8084)
            .setHost("localhost")
            .build().delete();
        HttpClient.getBuilder()
            .setPath("/__admin/requests")
            .setPort(8082)
            .setHost("localhost")
            .build().delete();
        HttpClient.getBuilder()
            .setPath("/__admin/requests")
            .setPort(8085)
            .setHost("localhost")
            .build().delete();
    }

    @BeforeAll
    public static void setup () throws IOException, InterruptedException {
        registerWithConsul(customersName, "127.0.0.1", 8085, "/health");
        registerWithConsul(accountsQueryName, "127.0.0.1", 8084, "/health");
        registerWithConsul(accountsCmdName, "127.0.0.1", 8082, "/health");
        Thread.sleep(1000);
    }

    @AfterAll
    public static void teardown () throws IOException {
        deregisterWithConsul(customersName);
        deregisterWithConsul(accountsQueryName);
        deregisterWithConsul(accountsCmdName);
    }

    private static void registerWithConsul(String serviceName, String host, int port, String path) throws IOException {
        URL obj = new URL(String.format("http://%s:%s/v1/agent/service/register", consulHost, consulPort));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String body = String.format("{\n"
            + "  \"ID\": \"%s1\",\n"
            + "  \"Name\": \"%s\",\n"
            + "  \"Tags\": [\n"
            + "    \"primary\",\n"
            + "    \"v1\"\n"
            + "  ],\n"
            + "  \"Address\": \"%s\",\n"
            + "  \"Port\": %d,\n"
            + "  \"EnableTagOverride\": false,\n"
            + "  \"Weights\": {\n"
            + "    \"Passing\": 10,\n"
            + "    \"Warning\": 1\n"
            + "  }\n"
            + "}", serviceName, serviceName, host, port, port, path);
        con.getOutputStream().write(body.getBytes("UTF8"));
        int statusCode = con.getResponseCode();
        if (statusCode == 200) {
            LOG.info(String.format("Registering %s with Consul, got response: %d", serviceName, statusCode));
        } else {
            LOG.error(String.format("Registering %s with Consul, got response: %d", serviceName, statusCode));
        }
    }

    private static void deregisterWithConsul(String serviceName) throws IOException {
        URL obj = new URL(String.format("http://%s:%s/v1/agent/force-leave/%s", consulHost, consulPort, serviceName));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("PUT");
        int statusCode = con.getResponseCode();
        if (statusCode == 200) {
            LOG.info(String.format("Deregistering %s with Consul, got response: %d", serviceName, statusCode));
        } else {
            LOG.error(String.format("Deregistering %s with Consul, got response: %d", serviceName, statusCode));
        }
    }
}
