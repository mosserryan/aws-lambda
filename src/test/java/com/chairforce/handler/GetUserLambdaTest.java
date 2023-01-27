package com.chairforce.handler;//package com.chairforce.handler;

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
public class GetUserLambdaTest {

    private final String VALID_PATH_PARAM_JSON = "{\"pathParameters\": {\"userId\": \"validId\"}}";

    @Mock private Context mockedContext;
    @Mock private LambdaLogger loggerMock;
    @Mock private UserUtil mockedUserUtil;

    private final GetUserLambda getUserLambda = new GetUserLambda();
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

        ReflectionTestUtils.setField(getUserLambda, "userUtil", mockedUserUtil);
    }

    @Test
    void handleRequest_validInput_validOutput() throws IOException {
        // Arrange
        inputStream = new ByteArrayInputStream(VALID_PATH_PARAM_JSON.getBytes());
        when(mockedUserUtil.getUserFromJson(anyString())).thenReturn(Optional.empty());

        // Act
        getUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 404;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody = "{\"user\":\"[]\"}";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log("Could not find specified User, with supplied input of: {\"userId\":\"validId\"}");
    }

    @Test
    void handleRequest_inValidInput_validOutput() throws IOException {
        // Arrange
        inputStream = new ByteArrayInputStream(VALID_PATH_PARAM_JSON.getBytes());
        when(mockedUserUtil.getUserFromJson(anyString())).thenReturn(Optional.of(new User()));
        when(mockedUserUtil.convertUserToJson(any(User.class))).thenReturn(
                "{\"userId\":\"validId\"" +
                ",\"firstName\":\"John\"" +
                ",\"lastName\":\"testUser\"" +
                ",\"email\":\"testUser\"," +
                "\"age\":\"10\" }");

        // Act
        getUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 200;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody = "{\"user\":\"{\"userId\":\"validId\"" +
                    ",\"firstName\":\"John\"" +
                    ",\"lastName\":\"testUser\"" +
                    ",\"email\":\"testUser\"" +
                    ",\"age\":\"10\" }\"}";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log("Successfully found the following User:" +
                " {\"userId\":\"validId\"," +
                "\"firstName\":\"John\"," +
                "\"lastName\":\"testUser\"," +
                "\"email\":\"testUser\"," +
                "\"age\":\"10\" }");
    }

    @Test
    void handleRequest_InValidInputParam() throws IOException {
        // Arrange
        String INVALID_PATH_PARAM_JSON = "{\"notValid\": {\"userId\": \"validId\"}}";
        inputStream = new ByteArrayInputStream(INVALID_PATH_PARAM_JSON.getBytes());

        // Act
        getUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        JsonObject response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        int expectedStatusCode = 404;
        int actualStatusCode = response.get("statusCode").getAsInt();
        String expectedBody = "{\"errorMessage\":\"Invalid request of {\"notValid\":{\"userId\":\"validId\"}}, must send a valid request pattern.\"}";
        String actualBody = response.get("body").getAsString();

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertEquals(expectedBody, actualBody);

        verify(mockedContext, times(1)).getLogger();
        verify(loggerMock, times(1)).log("Unable to validate invalid request of: {\"notValid\":{\"userId\":\"validId\"}}");
    }

}
