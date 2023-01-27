package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserLambdaTest {

    static GetUserLambda getUserLambda;
    @Mock
    Context mockedContext;
    @Mock
    LambdaLogger loggerMock;

    @BeforeEach
    public void setUp() {
        when(mockedContext.getLogger()).thenReturn(loggerMock);

        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

        getUserLambda = new GetUserLambda();
    }

    @Test
    public void testGetuserLambda() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GetUser");
        getUserLambda.handleRequest(inputStream, OutputStream.nullOutputStream(), mockedContext);
    }

}
