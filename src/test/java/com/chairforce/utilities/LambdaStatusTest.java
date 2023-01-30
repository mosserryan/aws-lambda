package com.chairforce.utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LambdaStatusTest {

    @Mock LambdaLogger mockLambdaLogger;
    LambdaStatus instance;

    @BeforeEach
    public void setup () throws Exception {
        resetSingleton();
        instance = LambdaStatus.getInstance();
    }

    @Test
    @DisplayName("Test that LambdaStatus is a singleton")
    public void testSingleton() {
        // Arrange
        LambdaStatus instance1 = LambdaStatus.getInstance();
        LambdaStatus instance2 = LambdaStatus.getInstance();

        // Act
        int instance1Hash = instance1.hashCode();
        int instance2Hash = instance2.hashCode();

        // Assert
        assertEquals(instance1Hash, instance2Hash);
    }

    @Test
    public void testSetLambdaLogger() {
        // Arrange - Act
        instance.setLambdaLogger(mockLambdaLogger);

        // Assert
        assertEquals(mockLambdaLogger, instance.getLambdaLogger());
    }

    @Test
    public void testLog() {
        // Arrange
        instance.setLambdaLogger(mockLambdaLogger);

        // Act
        instance.log("test message");

        // Assert
        verify(mockLambdaLogger).log("test message");
    }

    public void resetSingleton() throws Exception {
        Field instance = LambdaStatus.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }

}
