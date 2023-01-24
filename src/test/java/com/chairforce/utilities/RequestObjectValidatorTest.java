package com.chairforce.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class RequestObjectValidatorTest {

    LambdaStatus lambdaStatus = LambdaStatus.getInstance();

    @Test
    @DisplayName("Returns true when valid pathParameters are passed.")
    public void testRequestObjectValidator_withPathParameters_returnsTrue() {
        //Arrange
        String getUserJson = "{\"pathParameters\": {\"userId\": \"valid Id\"}}";
        JsonObject getUserRequest = JsonParser.parseString(getUserJson).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(getUserRequest);
        JsonObject actual = JsonParser.parseString("{\"userId\":\"valid Id\"}").getAsJsonObject();

        //Assert
        assertEquals(lambdaStatus.getRequestObj(), actual);
        assertTrue(isSuccessful);
    }

    @Test
    @DisplayName("Returns true when valid request body is passed.")
    public void testRequestObjectValidator_withRequestBody_returnsTrue() {
        //Arrange
        String createUserJson =
                "{\"body\": {\"firstName\": \"Ryan\",\"lastName\": \"Mosser\",\"email\": \"ryanM@chairforce.com\",\"age\": \"30\"}}";
        JsonObject createUserRequest = JsonParser.parseString(createUserJson).getAsJsonObject();

        // Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(createUserRequest);
        JsonObject actual = lambdaStatus.getRequestObj();
        JsonObject expected = JsonParser.parseString(
                "{\"firstName\": \"Ryan\"," +
                        "\"lastName\": \"Mosser\"," +
                        "\"email\": \"ryanM@chairforce.com\"," +
                        "\"age\": \"30\"}").getAsJsonObject();

        //Assert
        assertEquals(expected, actual);
        assertTrue(isSuccessful);
    }

    @Test
    @DisplayName("Returns false when request pattern is invalid.")
    public void testRequestObjectValidator_returnsFalse_whenRequestPatternInvalid() {
        //Arrange
        String invalidJson = "{\"\": {\"userId\": \"valid Id\"}}";
        JsonObject invalidJsonObj = JsonParser.parseString(invalidJson).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(invalidJsonObj);
        String expected = "Must send a valid request pattern.";
        String actual = lambdaStatus.getStatusMsg();

        //Assert
        assertFalse(isSuccessful);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Returns false when `userId` key is missing from input stream.")
    public void testRequestObjectValidator_withValidPathParameters_returnsFalse_whenKeyInvalid() {
        //Arrange
        String invalidJson = "{\"pathParameters\": {\"\": \"valid Id\"}}";
        JsonObject invalidJsonObj = JsonParser.parseString(invalidJson).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(invalidJsonObj);
        String expected = "Expected key of `userId` is missing.";
        String actual = lambdaStatus.getStatusMsg();

        //Assert
        assertFalse(isSuccessful);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Returns false when key value is null or missing.")
    public void testRequestObjectValidator_withValidKey_returnsFalse_whenValueInvalid() {
        //Arrange
        String invalidJson = "{\"pathParameters\": {\"userId\": \"\"}}";
        JsonObject invalidJsonObj = JsonParser.parseString(invalidJson).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(invalidJsonObj);
        String expected = "Key value of `userId` cannot be null or empty.";
        String actual = lambdaStatus.getStatusMsg();

        //Assert
        assertFalse(isSuccessful);
        assertEquals(expected, actual);
    }

}
