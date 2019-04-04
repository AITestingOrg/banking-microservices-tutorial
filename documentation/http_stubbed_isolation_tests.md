# Create a Service Isolation Tests
This page documents how to create service isolation tests. Since the service runs in isolation, no Docker configuration is needed for this guide.
## Create a Test Fixture
We use [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) for this project (Account Transactions), so there is nothing JUnit related that you will need to do in order to create a test fixture other than creating a new Java class.
```java
public class DepositServiceIsolationTest {
    ...
}
```
In order for the test to have access to the injection service as well as run and bind the service under test before the test executes we will need to use a [Micronaut](https://micronaut.io/) annotation.
The [MicronautTest](https://micronaut-projects.github.io/micronaut-test/latest/guide/index.html) annotation, when applied to the test fixture, 
will run and bind the default port configured for service under test before the tests execute. If you are familiar with [SpringBootTest](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html), this is very similar.
```java
@MicronautTest
public class DepositServiceIsolationTest {
    ...
}
```
By default, when running with the MicronautTest annotation, the service will execute under the `test` [Environment](), which is similar to Spring's [Profile](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) feature. 
The environments allow different configurations to be executed at runtime. For service isolation tests in this project, there are two methods which the test can be run, with and without Mongo instances. The `external_mocks` environment will enforce the injection of a 
`MongoClient` adapter that is connected to an in-process Mongo instance, essentially an in-memory DB. Not using the `external_mocks` project leads to the default MongoClient being used against a real Mongo instance.
It should be noted, with or without the in-memory DB all of the code, including Micronaut's Mongo wrapper will still be executed, it is only the DB instance that changes with these two configurations.
You can force profiles with the `MicronautTest` annotation.
```java
@MicronautTest(environments = {"external_mocks"})
public class DepositServiceIsolationTest extends MockedHttpDependencies {
    ...
}
```
For now, it will be more convenient to execute the tests without a live Mongo instance so we will leave the `external_mocks` environment in place.

#### Import Note about Service Isolation Tests and Communication with Other Services
Note the class that this test fixture extends from, since this is a microservices ecosystem and this service depends on other services, we cannot simply execute actions on this service without its dependencies. Instead, we provide its dependencies as mocks using [WireMock](http://wiremock.org/).
The `MockedHttpDependencies` takes care of the logic here providing the test with Mock services running on their default ports off of the test process. However, this does not mock Consul, the discovery service, under the `application-test.yml` we manually disable Consul registry from the service under test and provide configurations to inject the urls which the dependencies are running on.

These mocked services do not have endpoints out of the box, so HTTP stubs are provided via ./resources/wiremock directory as JSON files. There are 200 and 404 status code cases available, but these can be extended by simply providing additional JSON files under each service within this directory.

For all of the tests we are about to create, the service under tests need to verify if a customer exists within another service. Thereforce, the expected endpoint is stubbed witht eh following JSON configuration for WireMock.
```json
/* file: .resources/wiremock/people/mappings/get-success-stub.json */
{
  "request": {
    "method": "GET",
    "url": "/api/v1/people/5c8ffe2b7c0bec3538855a0a"
  },
  "response": {
    "status": 200,
    "body": "{\"id\": \"5c8ffe2b7c0bec3538855a0a\",\"firstName\": \"Rodney\",\"lastName\": \"Mckay\"}",
    "headers": {
      "Content-Type": "application/json"
    }
  }
}
```
Now, all we need to do is use the `5c8ffe2b7c0bec3538855a0a` ID from our mock. We do the same for the accounts service, you can find the mocks for that service under `./resources/wiremock/accountquery/mappings`.

## Triggering the Action Under Test
The service isolation tests differ from the unit and integration tests at the class level as the service isolation test triggers the action to be tested via its external interface. In this case, 
that interface is the Account Transaction service REST API. To make calls against this API we have provided a helper object called `HttpClient`, the following code snippet demonstrates it's initialization.
```java
...
private static HttpClient client;

@BeforeAll
public static void beforeAll() {
    client = HttpClient.getBuilder()
        .setHost("localhost")
        .setPort(8086)
        .setPath("/api/v1/transactions")
        .build();
}
...
```
Here, we use the builder pattern to create an HTTP Client that submits HTTP requests to localhost, on port 8086 (which the Account Transactions services uses) and establish the base HTTP path. Note, it is usually a better practice to provide these hardcodeed values through some configuration, but they are provided here as literals for simplicity.

Now, we can write a test that makes calls to the service under test, which returns our helpers Response object. Below, the code demonstrates
how to make a RESTful call to the service's `/api/v1/transactions/deposit` endpoint which should result in a 201 status code (Created) and return a transaction ID.
```java
@Test
public void givenValidAccount_whenDepositing_thenTransactionIdReturned() throws IOException {
    // Arrange
    TransactionDto transactionDto = new TransactionDto(customerId, accountId, 15.00);
    
    // Act
    ResponseDto response = client.post(transactionDto, "/deposit");
    
    // Assert
    assertEquals(201, response.getStatusCode());
    assertTrue(ObjectId.isValid(response.getBody().replace("\n", "")));
}
```

## Asserting Against Mocked Services
Next, we will test if the Account Command service received the deposit request, in this case, we verify if the mock received precisely one request to the credit endpoint.
Note, the HTTP client on the service under test is fully exercised here as the Mock is running on another thread, so an HTTP call is actually made.

```java
public void givenValidAccount_whenDepositing_thenTheAccountCmdServiceIsCalled() throws IOException {
    // Arrange
    TransactionDto transactionDto = new TransactionDto(customerId, accountId, 15.00);
    
    // Act
    ResponseDto response = client.post(transactionDto, "/deposit");
    
    // Assert
    accountCmdService.verify(1, putRequestedFor(urlEqualTo("/api/v1/accounts/credit")));
}
```

## Injecting Components into the Tests
Micronaut provides a rich dependency injection framework that carries over to testing. This allows for spying on internals or instrumenting complex conditions before tests execute.
The following example shows how the Mongo adapter is injected into the test fixture to perform data setup in the test.
```java
@MicronautTest(environments = {"external_mocks"})
public class DepositServiceIsolationTest extends MockedHttpDependencies {
    @Inject
    private Repository<Transaction> mongoRepository;
    private String accountId = "5c8ffe2b7c0bec3538855a06";
    private String customerId = "5c8ffe2b7c0bec3538855a0a";
    
    @BeforeEach
    private void beforeEach() {
        mongoRepository.add(new Transaction(accountId, customerId, 20.00));
    }
    
    @AfterEach
    private void afterEach() {
        mongoRepository.dropCollection();
    }
}
```
The above example performs simple setup and tear down operations, but this injection allows for more complicated, internal logic to be actuated from tests that may be otherwise difficult to instrument.
However, for this test we will not need these before or after methods, nor the repository.


## Final Code
```java
package com.ultimatesoftware.banking.account.transactions.tests.service.isolation;

import com.ultimatesoftware.banking.account.transactions.models.TransactionDto;
import com.ultimatesoftware.banking.test.utils.HttpClient;
import com.ultimatesoftware.banking.test.utils.MockedHttpDependencies;
import com.ultimatesoftware.banking.test.utils.ResponseDto;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(environments = {"external_mocks"})
public class DepositServiceIsolationTest extends MockedHttpDependencies {
    private static final String accountId = "5c8ffe2b7c0bec3538855a06";
    private static final String customerId = "5c8ffe2b7c0bec3538855a0a";
    private static HttpClient client;

    @BeforeAll
    public static void beforeAll() {
        client = HttpClient.getBuilder()
            .setHost("localhost")
            .setPort(8086)
            .setPath("/api/v1/transactions")
            .build();
    }

    @Test
    public void givenValidAccount_whenDepositing_thenTransactionIdReturned() throws IOException {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(customerId, accountId, 15.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/deposit");

        // Assert
        assertEquals(201, response.getStatusCode());
        assertTrue(ObjectId.isValid(response.getBody().replace("\n", "")));
    }

    @Test
    public void givenValidAccount_whenDepositing_thenTheAccountCmdServiceIsCalled() throws IOException {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(customerId, accountId, 15.00);

        // Act
        ResponseDto response = client.post(transactionDto, "/deposit");

        // Assert
        accountCmdService.verify(1, putRequestedFor(urlEqualTo("/api/v1/accounts/credit")));
    }
}

```

## Summary
In this guide, we learned how to use the Micronaut framework to create a service isolation test with Mocked service dependencies. We also learned about the different environments required to put the system into a configuration which we can run the service in a stand-alone manner. One of the primary
benefits of this test is that it runs quickly and without many infrastructure requirements, such as completed dependencies or cloud infrastructure.
