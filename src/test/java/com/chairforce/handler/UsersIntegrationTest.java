package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersIntegrationTest {

    @Mock
    private Context mockedContext;
    @Mock private LambdaLogger loggerMock;

    private final CreateUserLambda createUserLambda = new CreateUserLambda();
    private final GetUserLambda getUserLambda = new GetUserLambda();
    private final DeleteUserLambda deleteUserLambda = new DeleteUserLambda();
    private OutputStream outputStream;
    private InputStream inputStream;
    private JsonObject response;
    private String createUser;
    private String createUserId;
    private String getUser;
    private String getUserId;
    private String deleteUser;
    private String deleteUserId;

    @BeforeEach
    public void setUp() {
        when(mockedContext.getLogger()).thenReturn(loggerMock);
        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());
    }

    @Test
    @DisplayName("Tests all handlers synchronously")
    public void testHandleRequest_create_get_delete() throws IOException {

        createUser();
        getUser();
        deleteUser();

        assertEquals(createUserId, getUserId);
        assertEquals(getUserId, deleteUserId);
        assertEquals(deleteUserId, createUserId);

        assertEquals(createUser, getUser);
        assertEquals(getUser, deleteUser);
        assertEquals(deleteUser, createUser);

    }

    private void createUser() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStream = getClass().getClassLoader().getResourceAsStream("CreateUser");
        createUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        createUser = buildUserString();
        createUserId = builderUserIdString();
    }

    private void getUser() throws IOException {
        outputStream = new ByteArrayOutputStream();
        String getUserJson = "{\"pathParameters\": {\"userId\": " + createUserId +"}}";
        inputStream = new ByteArrayInputStream(getUserJson.getBytes());
        getUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        getUser = buildUserString();
        getUserId = builderUserIdString();
    }

    private void deleteUser() throws IOException {
        outputStream = new ByteArrayOutputStream();
        String deleteUserJson = "{\"pathParameters\": {\"userId\": " + getUserId +"}}";
        inputStream = new ByteArrayInputStream(deleteUserJson.getBytes());
        deleteUserLambda.handleRequest(inputStream, outputStream, mockedContext);
        deleteUser = buildUserString();
        deleteUserId = builderUserIdString();
    }

    private String buildUserString() {
        response = JsonParser.parseString(outputStream.toString()).getAsJsonObject();
        response = JsonParser.parseString(response.get("body").getAsString()).getAsJsonObject();
        return response.get("user").toString();
    }

    private String builderUserIdString() {
        response = response.getAsJsonObject("user");
        return response.get("userId").getAsString();
    }

}
