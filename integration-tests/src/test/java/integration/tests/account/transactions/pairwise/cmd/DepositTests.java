package integration.tests.account.transactions.pairwise.cmd;

import integration.tests.utils.HttpStub;
import integration.tests.utils.RestHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static integration.tests.utils.AssertHelper.assertValueInJsonField;
import static integration.tests.utils.MockHttpConstants.VALID_PERSON_ID;
import static io.restassured.RestAssured.given;

public class DepositTests {
    private final RestHelper restHelper = new RestHelper();
    private String accountId1;

    @BeforeEach
    public void beforeAll() {
        accountId1 = restHelper.createAccount(VALID_PERSON_ID);
        restHelper.pushMappingToAccountQueryMock(HttpStub.builder()
            .setRequestType("GET")
            .setPath("/api/v1/accounts/" + accountId1)
            .setContentType("application/json")
            .setStatus(200)
            .setBody(String.format("{\"id\": \"%s\",\"customerId\": \"%s\",\"balance\": %.2f}", accountId1, VALID_PERSON_ID, 0.0))
            .build());
    }

    @AfterEach
    public void afterEach() {
        restHelper.clearAccounts();
    }

    @Test
    public void givenCorrectInput_whenDepositing_thenTransactionIsMarkedSuccessful() {
        // Arrange
        String transaction = "{\n"
            + "\t\"accountId\": \"%s\",\n"
            + "\t\"customerId\": \"%s\",\n"
            + "\t\"amount\": 100.0\n"
            + "}";
        RestAssured.baseURI = "http://localhost:" + RestHelper.TRANSACTION_PORT;

        // Act
        Response transactionResponse = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Assert
        String transactionId = transactionResponse.getBody().asString();
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/transactions/id/" + transactionId);

        response.then().statusCode(200);
        assertValueInJsonField(response, "status", "SUCCESSFUL");
    }
}
