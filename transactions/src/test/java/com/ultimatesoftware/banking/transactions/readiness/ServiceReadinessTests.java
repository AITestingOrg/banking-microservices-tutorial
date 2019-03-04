package com.ultimatesoftware.banking.transactions.readiness;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ServiceReadinessTests {

    @BeforeEach
    public void setup() throws Exception {
        Thread.sleep(1);
    }

    @Test
    public void health_service_readiness_endpoint_returns_up() {
        given().
            accept("application/json").
        when().
                get("http://localhost:8086/health").
                then().
                statusCode(200).
                body("status", equalTo("UP"));

    }

    @Test
    public void health_service_readiness_endpoint_returns_correct_mongo_status() {
        RequestSpecification request = given().
            accept("application/json");
        Response response = request.when().
            get("http://localhost:8086/health");

        response.
            then().
            statusCode(200).
            body("mongo.status", equalTo("UP"),
                 "mongo.version", equalTo("3.4.1"));

    }

    @Test
    public void health_service_readiness_endpoint_returns_correct_rabbitmq_status() {
        given().
            accept("application/json").
        when().
                get("http://localhost:8086/health").
                then().
                statusCode(200).
                body("rabbit.status", equalTo("UP"),
                        "rabbit.version", equalTo("3.7.8"));

    }

    @Test
    public void metrics_service_readiness_endpoint_returns_correct_metrics() {
        given().
            accept("application/json").
        when().
                get("http://localhost:8086/metrics").
                then().
                statusCode(200).
                body("processors", greaterThan(1),
                        "mem", greaterThan(300000));

    }

    @Test
    public void health_service_readiness_endpoint_returns_correct_discorvery_service_status() {
        given().
            accept("application/json").
        when().
                get("http://localhost:8086/health").
                then().
                statusCode(200).
                body("discoveryComposite.eureka.status", equalTo("UP"));

    }
}
