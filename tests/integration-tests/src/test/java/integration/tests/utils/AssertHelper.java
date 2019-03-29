package integration.tests.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import static integration.tests.utils.MockHttpConstants.delta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertHelper {

    public static void assertValueInJsonField(Response response, String fieldName, double value) {
        assertValueInJsonField(response, fieldName, value, 0.001f);
    }

    public static void assertValueInJsonField(Response response, String fieldName, double value, double delta) {
        String bodyStringValue = response.getBody().asString();

        assertTrue(bodyStringValue.contains(fieldName), String.format("Assert response body contains field '%s' in body '%s'.", fieldName, bodyStringValue));
        DocumentContext cd = JsonPath.parse(bodyStringValue);

        double valueFound = cd.read("$['" + fieldName + "']", Double.class);

        assertTrue(delta(value, valueFound) <= delta, String.format("Assert '%.2f' is equal to '%.2f' with delta '%.2f'.", valueFound, value, delta));
    }

    public static void assertValueInJsonField(Response response, String fieldName, String value) {
        String bodyStringValue = response.getBody().asString();

        assertTrue(bodyStringValue.contains(fieldName), String.format("Assert response body contains field '%s' in body '%s'.", fieldName, bodyStringValue));
        DocumentContext cd = JsonPath.parse(bodyStringValue);

        String valueFound = cd.read("$['" + fieldName + "']", String.class);

        assertEquals(value, valueFound, String.format("Assert '%s' is equal to '%s'.", valueFound, value));
    }
}
