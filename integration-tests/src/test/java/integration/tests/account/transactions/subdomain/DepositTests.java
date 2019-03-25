package integration.tests.account.transactions.subdomain;

import integration.tests.utils.RestHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static integration.tests.utils.MockHttpConstants.VALID_PERSON_ID;
import static integration.tests.utils.MockHttpConstants.delta;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Requires docker-compose-sub-domain-testing.yml be up.
public class DepositTests {
    private static final String TRANSACTION_PORT = "8086";
    private static final String ACCOUNT_QUERY_PORT = "8084";
    private final RestHelper restHelper = new RestHelper();
    private String accountId1;
    private String accountId2;

    @BeforeEach
    public void beforeAll() {
        accountId1 = restHelper.createAccount(VALID_PERSON_ID);
    }

    @AfterEach
    public void afterEach() {
        restHelper.clearAccounts();
    }

    @Test
    public void givenCorrectInput_whenDepositing_thenAccountIsUpdated() {
        // Arrange
        String transaction = "{\n"
            + "\t\"accountId\": \"%s\",\n"
            + "\t\"customerId\": \"%s\",\n"
            + "\t\"amount\": 10.0\n"
            + "}";

        // Act
        RestAssured.baseURI = "http://localhost:" + TRANSACTION_PORT;
        given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Assert
        RestAssured.baseURI = "http://localhost:" + ACCOUNT_QUERY_PORT;
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/accounts/" + accountId1);

        String bodyStringValue = response.getBody().asString();

        assertTrue(bodyStringValue.contains("balance"));
        JsonPath jsonPath = response.jsonPath();
        float balance = jsonPath.get("balance");

        assertTrue(delta(10.0f, balance) <= 0.001);
    }
}
