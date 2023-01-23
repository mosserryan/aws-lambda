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
    @DisplayName("Returns true when valid input stream is passed.")
    public void testRequestObjectValidator_returnsTrue() throws FileNotFoundException {
        //Arrange
        InputStream validInput = new FileInputStream("src/test/resources/GetUser");
        BufferedReader reader = new BufferedReader(new InputStreamReader(validInput));
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(jsonObject);
        JsonObject actual = JsonParser.parseString("{\"userId\":\"valid Id\"}").getAsJsonObject();

        //Assert
        assertEquals(lambdaStatus.getRequestObj(), actual);
        assertTrue(isSuccessful);
    }

    @Test
    @DisplayName("Returns false when request pattern is invalid.")
    public void testRequestObjectValidator_returnsFalse_whenRequestPatternInvalid() throws FileNotFoundException {
        //Arrange
        InputStream invalidInput = new FileInputStream("src/test/resources/GetUser_Invalid_PathParameters");
        BufferedReader reader = new BufferedReader(new InputStreamReader(invalidInput));
        JsonObject invalidJsonObj = JsonParser.parseReader(reader).getAsJsonObject();

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
    public void testRequestObjectValidator_withValidPathParameters_returnsFalse_whenKeyInvalid() throws FileNotFoundException {
        //Arrange
        InputStream invalidInput = new FileInputStream("src/test/resources/GetUser_Invalid_UserId_Key");
        BufferedReader reader = new BufferedReader(new InputStreamReader(invalidInput));
        JsonObject invalidJsonObj = JsonParser.parseReader(reader).getAsJsonObject();

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
    public void testRequestObjectValidator_withValidKey_returnsFalse_whenValueInvalid() throws FileNotFoundException {
        //Arrange
        InputStream invalidInput = new FileInputStream("src/test/resources/GetUser_Invalid_UserId_Value");
        BufferedReader reader = new BufferedReader(new InputStreamReader(invalidInput));
        JsonObject invalidJsonObj = JsonParser.parseReader(reader).getAsJsonObject();

        //Act
        boolean isSuccessful = RequestObjectValidator.validateRequest(invalidJsonObj);
        String expected = "Key value of `userId` cannot be null or empty.";
        String actual = lambdaStatus.getStatusMsg();

        //Assert
        assertFalse(isSuccessful);
        assertEquals(expected, actual);
    }

}
