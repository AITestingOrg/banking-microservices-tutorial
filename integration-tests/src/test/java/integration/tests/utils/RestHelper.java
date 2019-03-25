package integration.tests.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static integration.tests.utils.MockHttpConstants.VALID_PERSON_ID;
import static integration.tests.utils.MockHttpConstants.delta;
import static io.restassured.RestAssured.given;

public class RestHelper {
    private static final Logger logger = LoggerFactory.getLogger(RestHelper.class);
    private final List<String> accounts = new ArrayList<>();
    public static final String ACCOUNT_QUERY_PORT = "8084";
    public static final String TRANSACTION_PORT = "8086";
    public static final String ACCOUMT_CMD_PORT = "8082";

    public void clearAccounts() {
        for (String id: accounts) {
            checkAndClearBalance(id);
            deleteAccount(id);
        }
        accounts.clear();
    }

    private void checkAndClearBalance(String id) {
        RestAssured.baseURI = "http://localhost:" + ACCOUNT_QUERY_PORT;
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/accounts/" + id);

        String bodyStringValue = response.getBody().asString();

        if (!bodyStringValue.contains("balance")) {
            logger.info(String.format("Account with ID %s not found for balance check.", id));
            return;
        }
        JsonPath jsonPath = response.jsonPath();
        float balance = jsonPath.get("balance");

        if (delta(0.0f, balance) > 0.0001) {
            String transaction = "{\n"
                + "\t\"accountId\": \"%s\",\n"
                + "\t\"customerId\": \"%s\",\n"
                + "\t\"amount\": %.2f\n"
                + "}";
            RestAssured.baseURI = "http://localhost:" + TRANSACTION_PORT;
            given().urlEncodingEnabled(true)
                .contentType(ContentType.JSON)
                .body(String.format(transaction, id, VALID_PERSON_ID, balance))
                .post("/api/v1/transactions/withdraw");
            logger.info(String.format("Account with ID %s found with balance, requested withdraw of %.2f.", id, balance));
        } else {
            logger.info(String.format("Account with ID %s found but balance is eligible for delete.", id));
        }
    }

    public String createAccount(String customerId) {
        return createAccount(customerId, 0.0);
    }

    public String createAccount(String customerId, double balance) {
        RestAssured.baseURI = "http://localhost:8082";
        String accountId =  given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format("{\"customerId\": \"%s\", \"balance\": %.2f}", customerId, balance))
            .post("/api/v1/accounts")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .body()
            .asString();
        logger.info("Created account with ID: " + accountId);
        accounts.add(accountId);
        return accountId;
    }

    public void deleteAccount(String accountId) {
        RestAssured.baseURI = "http://localhost:8082";
        given().urlEncodingEnabled(true)
            .delete("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(200);
        logger.info("Deleted account with ID: " + accountId);
    }
}
