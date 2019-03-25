package integration.tests.utils;

public class MockHttpConstants {
    public static final String VALID_PERSON_ID = "5c8ffe2b7c0bec3538855a0a";
    public static final String NOT_FOUND_PERSON_ID = "5c892aecf72465a56c4f816d";

    public static double delta(float d1, float d2) {
        return Math.abs(d1 - d2) / Math.max(Math.abs(d1), Math.abs(d2));
    }
}
