package com.chairforce.handler;//package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetUserIntegrationTest {

    @Mock private Context mockedContext;
    @Mock private LambdaLogger loggerMock;

    private final GetUserLambda getUserLambda = new GetUserLambda();
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
    public void testGetuserLambda() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GetUser");
        getUserLambda.handleRequest(inputStream, outputStream, mockedContext);
    }


}
