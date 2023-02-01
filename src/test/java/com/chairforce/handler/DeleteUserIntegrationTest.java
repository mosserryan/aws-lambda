package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteUserIntegrationTest {

    @Mock
    private Context mockedContext;
    @Mock private LambdaLogger loggerMock;

    private final DeleteUserLambda deleteUserLambda = new DeleteUserLambda();
    private OutputStream outputStream;

    @BeforeEach
    public void setUp() {
        when(mockedContext.getLogger()).thenReturn(loggerMock);
        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

        outputStream = new ByteArrayOutputStream();

    }

    @Test
    public void testDeleteUserLambda() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("DeleteUser");
        deleteUserLambda.handleRequest(inputStream, outputStream, mockedContext);
    }

}
