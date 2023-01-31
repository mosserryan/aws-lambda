package com.chairforce.request;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.chairforce.utilities.LambdaStatus;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class RequestWrapperTest {

    LambdaStatus lambdaStatus = LambdaStatus.getInstance();

    @Mock LambdaLogger loggerMock;

    @BeforeEach
    public void setUp() {

        lambdaStatus.setLambdaLogger(loggerMock);

        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

    }

    @Test
    @DisplayName("Returns true when valid pathParameters are passed.")
    public void testRequestObjectValidator_withPathParameters_returnsTrue() {
        //Arrange
        String getUserJson = "{\"pathParameters\": {\"userId\": \"valid Id\"}}";
        JsonObject getUserRequest = JsonParser.parseString(getUserJson).getAsJsonObject();
        RequestWrapper requestWrapper = new RequestWrapper(getUserRequest);

        //Act
        boolean isSuccessful = requestWrapper.isValidRequest();
        JsonObject actual = JsonParser.parseString("{\"userId\":\"valid Id\"}").getAsJsonObject();
        JsonObject expected = requestWrapper.getRequestObj();

        //Assert
        assertEquals(expected, actual);
        assertTrue(isSuccessful);
    }

    @Test
    @DisplayName("Returns true when valid request body is passed.")
    public void testRequestObjectValidator_withRequestBody_returnsTrue() {
        //Arrange
        String createUserJson =
                "{\"body\": {\"firstName\": \"Ryan\",\"lastName\": \"Mosser\",\"email\": \"ryanM@chairforce.com\",\"age\": \"30\"}}";
        JsonObject createUserRequest = JsonParser.parseString(createUserJson).getAsJsonObject();
        RequestWrapper requestWrapper = new RequestWrapper(createUserRequest);

        // Act
        boolean isSuccessful = requestWrapper.isValidRequest();
        JsonObject actual = requestWrapper.getRequestObj();
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
        RequestWrapper requestWrapper = new RequestWrapper(invalidJsonObj);

        //Act
        boolean isSuccessful = requestWrapper.isValidRequest();

        //Assert
        assertFalse(isSuccessful);
    }

    @Test
    @DisplayName("Returns false when `userId` key is missing from input stream.")
    public void testRequestObjectValidator_withValidPathParameters_returnsFalse_whenKeyInvalid() {
        //Arrange
        String invalidJson = "{\"pathParameters\": {\"\": \"valid Id\"}}";
        JsonObject invalidJsonObj = JsonParser.parseString(invalidJson).getAsJsonObject();
        RequestWrapper requestWrapper = new RequestWrapper(invalidJsonObj);

        //Act
        boolean isSuccessful = requestWrapper.isValidRequest();

        //Assert
        assertFalse(isSuccessful);
    }

    @Test
    @DisplayName("Returns false when key value is null or missing.")
    public void testRequestObjectValidator_withValidKey_returnsFalse_whenValueInvalid() {
        //Arrange
        String invalidJson = "{\"pathParameters\": {\"userId\": \"\"}}";
        JsonObject invalidJsonObj = JsonParser.parseString(invalidJson).getAsJsonObject();
        RequestWrapper requestWrapper = new RequestWrapper(invalidJsonObj);

        //Act
        boolean isSuccessful = requestWrapper.isValidRequest();

        //Assert
        assertFalse(isSuccessful);
    }

    @Test
    @DisplayName("Test convert request to json format.")
    public void testGetRequestAsJson() {
        //Arrange
        String getUserJson = "{\"pathParameters\": {\"userId\": \"valid Id\"}}";
        JsonObject getUserRequest = JsonParser.parseString(getUserJson).getAsJsonObject();
        RequestWrapper requestWrapper = new RequestWrapper(getUserRequest);

        //Act
        String actual = requestWrapper.getRequestAsJson();
        String expected = "{\"userId\":\"valid Id\"}";

        //Assert
        assertEquals(actual, expected);
    }

}
