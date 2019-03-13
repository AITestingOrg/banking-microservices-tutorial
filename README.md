# Banking Microservices Example

The Banking Microservices Example project is a small system used to show how microservices can be implemented with Netflix's Zuul / Eureka framework and Axon's Event Sourcing framework. The system can be run in multiple configurations using Docker.

## Architecture
![Build Status](documentation/services.png)
<p style="text-align: center;">Figure 1: Overall Banking Example architecture.</p>

![Build Status](documentation/communication.png)
<p style="text-align: center;">Figure 2: Flow of communication between domain architectures.</p>

## Configuration
The services can be configured in three ways, a local default configuration under each project resources/application.yml, a development coniguration under
resources/application-dev.yml, and the centralized configuration service.

## Requirements
See each services readme for detailed requirement information

### Compose
* https://docs.docker.com/compose/install/
* /data/db directory created and accessible to "everyone"

# Running the Project

## Start the Microservices
** Build JARs for each project (You will need to build a JAR anytime changes are made to a project, then rebuild either the container or all containers)
```bash
export SPRING_PROFILES_ACTIVE=default
# Assemble the binaries
./gradlew assemble
# Start the backing services: service discovery, configuration, authentication, edge service
docker-compose -f docker-compose-backing.yml up
# After the backing services have succesfully loaded, start the domain services
docker-compose up
```

## Start the Microservices with ELK Stack
```bash
# Assemble the binaries
gradle assemble
# Start the backing services: service discovery, configuration, authentication, edge service
docker-compse -f docker-compose-backing.yml up
# While the backing services are starting, start the ELK stack, note you will need to also follow the ELK steps below
docker-compose -f elk/docker-compose.yml up
# Once all the supporting services are loaded, start the domain services configured to log to ELK
docker-compose -f docker-compose-elk.yml up
```

## Stop the Containers
```bash
docker-compose down
docker-compose -f docker-compose-elk.yml down
docker-compose -f docker-compose-backing.yml down

```

## Rebuild Containers
```bash
docker-compose build
```

# Executing Tests

## Running Unit Tests
The Gradle task 'test' executes the JUnit tests for each project.
```bash
sh u-unit-test.sh
```

## Running Code Coverage: Unit
JaCoCo is used for code coverage and can be run after the unit and integraiton tests for each service have been executed.
You can find a JaCoCo coverage report under the "coverage" in transaction service after running the unit tests.

## Running Integration Tests (No cross service calls)
The Gradle task 'test' executes the JUnit tests for each project.
```bash
sh u-integration-test.sh
```

## Running Contract Tests
```bash
sh u-contract-test.sh
```

## Running Service Readiness Tests
```bash
sh u-service-readiness-test.sh
```

## Running Edge API Tests
```bash
sh u-e2e-test.sh
```

## PACT Consumer Tests
Ensure the PACT Broker is live
```bash
cd misc-docker-images
docker-compose up
```
Build and execute PACT consumer tests, this will first execute the consumer tests in the consumer project against a mock provider to generate the PACTs, the PACTs will then be executed against the actual provider and finally published to the PACT broker.
```bash
sh pact-tests.sh
```
Go to `http://localhost:8081` to view the contracts that have been published.
## Service Readiness Endpoints
Spring Actuator provides a series of service readiness endpoints which provides links to various metrics at `service:port/actuator`.

# API Documentation:

These request can be done using an application like postman or insomnia, directly with curl or using the provided swagger UI.
Go to the [swagger](http://localhost:8082/swagger-ui.html) for the port that customer application is running. By default, it is 8082 but it can be changed in the docker-compose files.

![alt text](images/customer-swagger.png "Swagger")

From there click on the customer-controller drop down, expand the post endpoint and click the try it out button:
![alt text](images/customer-create.png "Post customer")

Then a body can be provided to make a request to the service, here is an example valid body, feel free to put your name here:
```json
{
     "firstName": "John",
     "id": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
     "lastName": "Doe"
}
```

Then click on the execute button

![alt text](images/customer-post.png "Post customer")

And scroll down to see what the response was:

![alt text](images/customer-response.png "Response")
Now to create some accounts for this user: 

Go to the ui for account-cmd, running on 8089. And open account-controller post [endpoint](http://localhost:8089/swagger-ui.html#/account-controller/addAccountUsingPOST)

Put the previous customerId in the body for the request and execute it. 

![alt text](images/account-post.png "create account")
The response should have the generated Id for the account just created. 
Copy it somewhere, then execute again and copy the second account id too, both will be used for transactions in a moment.

First quickly check the accounts got created checking the [account query side](http://localhost:8084/swagger-ui.html#/account-controller/getAccountUsingGET)

Check the two account ids against the get endpoint, they return a 200 response with the account info and balances of 0.
![alt text](images/account-get.png "check accounts")

Now, making some transactions lets first make a [deposit](http://localhost:8086/swagger-ui.html#/actions-controller/depositUsingGET)

Provide an amount along with the previous obtained ids for account and customer. This will respond with a transaction id that is not important for now. 
![alt text](images/transaction-deposit.png "put some money in")

If the same account is now check on the account query side the balance should shown as the deposited account.

Now, going to the transfer endpoint and making a transaction to pass some of that balance to the other account
![alt text](images/transaction-transfer.png "transfer some money")

Check that the response was a 200 and the balances changed

## Running with Centralized Logging (ELK stack)
To run with centralized logging and logging visualizations follow the steps below.

### Start the ELK stack
* `cd elk`
* `docker-compose up` wait for everything to start
* Check that http://localhost:5601 is accessible in your browser, you can read about configuration Kibana here https://www.elastic.co/guide/en/kibana/4.0/setup.html
* `cd ../`
* `docker-compose up -f docker-compose-elk.yml`
* Refresh Kibana to see the logs.
