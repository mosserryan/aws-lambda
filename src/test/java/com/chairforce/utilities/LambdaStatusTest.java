package com.chairforce.utilities;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaStatusTest {

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
    @DisplayName("Test setter and getter for request object")
    public void testSetAndGetRequestObj() {
        //Arrange
        JsonObject requestObj = new JsonObject();
        requestObj.addProperty("key", "value");
        LambdaStatus instance = LambdaStatus.getInstance();

        // Act
        instance.setRequestObj(requestObj);

        // Assert
        assertEquals(requestObj, instance.getRequestObj());
    }

    @Test
    @DisplayName("Test setter and getter for request object")
    public void testSetAndGetRequestObjAsString() {
        //Arrange
        JsonObject requestObj = new JsonObject();
        requestObj.addProperty("key", "value");
        LambdaStatus instance = LambdaStatus.getInstance();

        // Act
        instance.setRequestObj(requestObj);

        // Assert
        assertEquals(requestObj.toString(), instance.getRequestObjAsString());
    }

}
