package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.chairforce.entities.User;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserLambdaTest {

    private final String VALID_PATH_PARAM_JSON = "{\"pathParameters\": {\"userId\": \"validId\"}}";

    @Mock private Context mockedContext;
    @Mock private LambdaLogger loggerMock;
    @Mock private UserUtil mockedUserUtil;

    private final DeleteUserLambda deleteUserLambda = new DeleteUserLambda();
    private InputStream inputStream;
    private OutputStream outputStream;

    @BeforeEach
    public void setUp() {
        when(mockedContext.getLogger()).thenReturn(loggerMock);
        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

        outputStream = new ByteArrayOutputStream();

        ReflectionTestUtils.setField(deleteUserLambda, "userUtil", mockedUserUtil);
    }

    @Test
    void handleRequest_validInput_validOutput_Error404() throws IOException {
        // Arrange
        inputStream = new ByteArrayInputStream(VALID_PATH_PARAM_JSON.getBytes());
        when(mockedUserUtil.deleteUserFromJson(anyString())).thenReturn(Optional.empty());

        // Act
        deleteUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 404;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody = "Resource not found";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log(
                "Could not find specified User, with supplied input of: {\"userId\":\"validId\"}");
    }

    @Test
    void handleRequest_validInput_validOutput_Success200() throws IOException {
        // Arrange
        inputStream = new ByteArrayInputStream(VALID_PATH_PARAM_JSON.getBytes());
        when(mockedUserUtil.deleteUserFromJson(anyString())).thenReturn(Optional.of(new User()));
        when(mockedUserUtil.convertUserToJson(any(User.class))).thenReturn(
                "{\"userId\":\"validId\"" +
                ",\"firstName\":\"John\"" +
                ",\"lastName\":\"testUser\"" +
                ",\"email\":\"testUser\"," +
                "\"age\":\"10\" }");

        // Act
        deleteUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 200;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody =
                "{\"user\":{\"userId\":\"validId\"" +
                ",\"firstName\":\"John\"" +
                ",\"lastName\":\"testUser\"" +
                ",\"email\":\"testUser\"" +
                ",\"age\":\"10\"}}";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log("Successfully deleted the following User:" +
                " {\"userId\":\"validId\"," +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"testUser\"," +
                "\"email\":\"testUser\"," +
                "\"age\":\"10\" }");
    }

    @Test
    void handleRequest_InValidInputParam_Error400() throws IOException {
        // Arrange
        String INVALID_PATH_PARAM_JSON = "{\"notValid\": {\"userId\": \"validId\"}}";
        inputStream = new ByteArrayInputStream(INVALID_PATH_PARAM_JSON.getBytes());

        // Act
        deleteUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 400;
        int actualStatusCode = response.get("statusCode").getAsInt();
        System.out.println(response);
        String expectedBody = "Invalid request";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log(
                "Unable to validate invalid request of: {\"notValid\":{\"userId\":\"validId\"}}");
    }

}
