package com.ultimatesoftware.banking.edgeservice.system;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerPort;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import com.ultimatesoftware.banking.edgeservice.EdgeServiceApplication;
import com.ultimatesoftware.banking.edgeservice.utils.TestConstants;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;

public class AccountQueryTest {
    protected static final Logger LOG = LoggerFactory.getLogger(AccountQueryTest.class);

    private static String accountQueryURL;
    private static String accountCmdURL;
    private static String edgeServiceURL;
//
//    //Wait for all services to have ports open
//    @ClassRule
//    public static DockerComposeRule docker = DockerComposeRule.builder().pullOnStartup(true)
//            .file("src/test/resources/docker-compose.yml")
//            .waitingForService("mongo", HealthChecks.toHaveAllPortsOpen())
//            .waitingForService("rabbitmq", HealthChecks.toHaveAllPortsOpen())
//            .waitingForService("accountquery", HealthChecks.toHaveAllPortsOpen())
//            .waitingForService("discoveryservice", HealthChecks.toHaveAllPortsOpen())
//            .waitingForService("discoveryservice", HealthChecks.toRespondOverHttp(TestConstants.SERVICE_DISCOVERY_PORT,
//                    (port) -> port.inFormat(String.format("http://localhost:%s", TestConstants.SERVICE_DISCOVERY_PORT))))
//            .build();
//
//    //Get IP addresses and ports to run tests on
//    @BeforeClass
//    public static void initialize() {
//        LOG.info("Initializing ports from Docker");
//        DockerPort edgeServiceCommand = docker.containers().container("edgeservice")
//                .port(TestConstants.EDGE_SERVICE_PORT);
//        edgeServiceURL = String.format("http://%s:%s", edgeServiceCommand.getIp(),
//                edgeServiceCommand.getExternalPort());
//        LOG.info("EdgeService url found: " + edgeServiceURL);
//
//        LOG.info("Initializing ports from Docker");
//        DockerPort accountQueryCommand = docker.containers().container("accountquery")
//                .port(TestConstants.ACCOUNT_QUERY_PORT);
//        accountQueryURL = String.format("http://%s:%s", accountQueryCommand.getIp(),
//                accountQueryCommand.getExternalPort());
//        LOG.info("Account Query url found: " + accountQueryURL);
//
//        DockerPort accountCmdCommand = docker.containers().container("accountcmd")
//                .port(TestConstants.ACCOUNT_COMMAND_PORT);
//        LOG.info("Account Cmd url found: " + accountQueryURL);
//
//        accountCmdURL = String.format("http://%s:%s", accountCmdCommand.getIp(),
//                accountCmdCommand.getExternalPort());
//    }
//
//    private TestRestTemplate restTemplate = new TestRestTemplate();
//    private String tripId;
//
//    @Test
//    public void accountCmd_POSTAccount_AccountCreatedInQuery() {
//        //given:
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//
//        String body = "{ \"customerId\": \"123e4567-e89b-12d3-a456-426655440000L\" }";
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//        //when:
//        ResponseEntity<String> response = restTemplate.postForEntity(accountQueryURL + "/api/v1/accounts", request, String.class);
//
//        //then:
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//    }

    /*@Test
    public void tripQueryGETSpecificTripRequestSuccess() throws JSONException {
        //given:
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        String body = "{ \"originAddress\": \"Weston, FL\", \"destinationAddress\": "
                + "\"Miami, FL\", \"userId\": \"123e4567-e89b-12d3-a456-426655440000\" }";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(tripCommandURL + "/api/v1/trip", request,
                String.class);
        assertThat(postResponse.getStatusCodeValue()).isEqualTo(201);

        JSONObject json = new JSONObject(postResponse.getBody());
        tripId = json.getString("id");

        //when:
        ResponseEntity<String> response = restTemplate.getForEntity(tripQueryURL + "/api/v1/trip/" + tripId,
                String.class);

        //then:
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void tripQueryGETAllTripsRequestSuccess() {
        //given:
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        //when:
        ResponseEntity<String> response = restTemplate.getForEntity(tripQueryURL + "/api/v1/trips", String.class);

        //then:
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }*/
}
