package com.chairforce.utilities;

import com.google.gson.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseBuilderTest {

    @Test
    public void testResponseBuilderWithSuccessBody() {
        // Arrange
        String user = "{\"userId\":\"userId\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"age\":10,\"email\":\"john@email.com\"}";
        JsonObject body = new JsonObject();
        body.add("user", JsonParser.parseString(user));
        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("statusCode", 200);
        expectedResponse.addProperty("body", body.toString());

        // Act
        JsonObject response = new ResponseBuilder()
                .setStatusCode(200)
                .setSuccessBody(user)
                .build();

        //Assert
        assertEquals(response, expectedResponse);
    }

    @Test
    public void testResponseBuilderWithErrorBody() {
        // Arrange
        String errorMsg = "Invalid Request";
        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("statusCode", 400);
        expectedResponse.addProperty("body", "Invalid Request");

        // Act
        JsonObject response = new ResponseBuilder()
                .setStatusCode(400)
                .setErrorBody(errorMsg)
                .build();

        // Assert
        assertEquals(response, expectedResponse);
    }
}
