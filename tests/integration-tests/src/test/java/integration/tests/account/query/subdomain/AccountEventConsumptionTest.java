package integration.tests.account.query.subdomain;

import com.ultimatesoftware.banking.account.query.models.Account;
import integration.tests.utils.RestHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static integration.tests.utils.MockHttpConstants.VALID_PERSON_ID;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Requires docker-compose-sub-domain-testing.yml be up.
public class AccountEventConsumptionTest {
    private static final Logger logger = LoggerFactory.getLogger(AccountEventConsumptionTest.class);
    private static final RestHelper restHelper = new RestHelper();

    @BeforeAll
    public static void beforeAll() {
        restHelper.onlyUseGateway();
    }

    @AfterEach
    public void afterEach() throws InterruptedException {
        restHelper.clearAccounts();
    }

    @Test
    public void givenAccountCreated_whenGettingAccount_ThenAccountIsFound() {
        // Arrange
        String customerId = VALID_PERSON_ID;
        double balance = 0;
        String accountId = restHelper.createAccount(customerId, balance);

        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId);


        // Assert
        Account account = response.then()
            .statusCode(200)
            .extract()
            .response()
            .as(Account.class);
        assertEquals(accountId, account.getId().toHexString());
        assertEquals(customerId, account.getCustomerId());
        assertEquals(balance, account.getBalance());
    }

    @Test
    public void givenAccountCreatedCalledWithBalanceGreaterThanZero_whenGettingAccount_ThenAccountIsFoundWithZeroBalance() {
        // Arrange
        String customerId = VALID_PERSON_ID;
        double balance = 50.00;
        String accountId = restHelper.createAccount(customerId, balance);

        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId);


        // Assert
        Account account = response.then()
            .statusCode(200)
            .extract()
            .response()
            .as(Account.class);
        assertEquals(accountId, account.getId().toHexString());
        assertEquals(customerId, account.getCustomerId());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void givenNoAccounts_whenGettingAccounts_thenEmptyListReturned() {
        // Arrange

        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/");

        // Assert
        List<Account> list = (List<Account>) response.then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);

        assertEquals(0, list.size());
    }

    @Test
    public void givenOneAccount_whenGettingAccounts_thenListOfOneReturned() {
        // Arrange
        restHelper.createAccount(VALID_PERSON_ID, 0.0);
        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/");

        // Assert
        List list =response.then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);
        assertEquals(1, list.size());
    }

    @Test
    public void givenThreeAccounts_whenGettingAccounts_thenListOfThreeReturned() {
        // Arrange
        restHelper.createAccount(VALID_PERSON_ID, 0.0);
        restHelper.createAccount(VALID_PERSON_ID, 0.0);
        restHelper.createAccount(VALID_PERSON_ID, 0.0);
        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response =  given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/");

        // Assert
        List list =response.then()
            .statusCode(200)
            .extract()
            .response()
            .as(List.class);
        assertEquals(3, list.size());
    }

    @Test
    public void givenAccountDeleted_whenGettingAccount_thenNoAccountReturned() {
        // Arrange
        String accountId = restHelper.createAccount(VALID_PERSON_ID, 0.0);
        restHelper.deleteAccount(accountId);

        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        given().urlEncodingEnabled(true)
            .get("/api/v1/accounts/" + accountId)
            .then()
            .statusCode(404);

        // Assert
    }
}
