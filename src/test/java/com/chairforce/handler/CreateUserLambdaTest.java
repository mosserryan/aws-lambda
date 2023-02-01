package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.chairforce.entities.User;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserLambdaTest {

    @Mock private Context mockedContext;
    @Mock private LambdaLogger loggerMock;
    @Mock private UserUtil mockedUserUtil;
    private final CreateUserLambda createUserLambda = new CreateUserLambda();
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

        ReflectionTestUtils.setField(createUserLambda, "userUtil", mockedUserUtil);
    }

    @Test
    @DisplayName("Test should return successfully created `user` and `200` status code")
    void handleRequest_validInput_validOutput_Success200() throws IOException {
        // Arrange
        String VALID_PATH_PARAM_JSON = "{\"pathParameters\": {\"userId\": \"validId\"}}";
        inputStream = new ByteArrayInputStream(VALID_PATH_PARAM_JSON.getBytes());
        when(mockedUserUtil.createUserFromJson(anyString())).thenReturn(new User());
        when(mockedUserUtil.convertUserToJson(any(User.class))).thenReturn(
                "{\"userId\":\"validId\"" +
                ",\"firstName\":\"John\"" +
                ",\"lastName\":\"testUser\"" +
                ",\"email\":\"testUser\"," +
                "\"age\":\"10\" }");

        // Act
        createUserLambda.handleRequest(inputStream, outputStream, mockedContext);
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
        verify(loggerMock, times(1)).log("Successfully created the following User:" +
                " {\"userId\":\"validId\"," +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"testUser\"," +
                "\"email\":\"testUser\"," +
                "\"age\":\"10\" }");
    }

    @Test
    @DisplayName("Test checks should return `invalid request` body and `400` error code")
    void handleRequest_InValidInput_Error400() throws IOException {
        // Arrange
        String invalidInput = "{ \"invalid\": {\"firstName\": \"John\"}}";
        inputStream = new ByteArrayInputStream(invalidInput.getBytes());

        // Act
        createUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 400;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody = "Invalid request";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log(
                "Invalid request of {\"invalid\":{\"firstName\":\"John\"}}, must send a valid request pattern.");
    }

}
