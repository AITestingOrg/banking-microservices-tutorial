package com.ultimatesoftware.banking.transactions.readiness;

import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ServiceReadinessTests {

    @Test
    public void
    health_service_readiness_endpoint_returns_up() {

        when().
                get("/health").
                then().
                statusCode(200).
                body("status", equalTo("UP"));

    }

    @Test
    public void
    health_service_readiness_endpoint_returns_correct_mongo_status() {

        when().
                get("/health").
                then().
                statusCode(200).
                body("mongo.status", equalTo("UP"),
                        "mongo.version", equalTo("3.4.1"));

    }

    @Test
    public void
    health_service_readiness_endpoint_returns_correct_rabbitmq_status() {

        when().
                get("/health").
                then().
                statusCode(200).
                body("rabbit.status", equalTo("UP"),
                        "rabbit.version", equalTo("3.7.8"));

    }

    @Test
    public void
    metrics_service_readiness_endpoint_returns_correct_metrics() {

        when().
                get("/metrics").
                then().
                statusCode(200).
                body("processors", greaterThan(2),
                        "mem", greaterThan(768000));

    }

    @Test
    public void
    health_service_readiness_endpoint_returns_correct_discorvery_service_status() {

        when().
                get("/health").
                then().
                statusCode(200).
                body("discoveryComposite.eureka.status", equalTo("UP"));

    }
}
