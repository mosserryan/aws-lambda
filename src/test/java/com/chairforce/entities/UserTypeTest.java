package com.chairforce.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTypeTest {

    List<String> pathParameterList = List.of("userId");
    List<String> requestBodyList = List.of("firstName", "lastName", "age", "email");

    @Test
    @DisplayName("Should return requestBodyList when `body` is the requestType")
    public void requiredUserValuesTest_returnsRequestBodyList() {
        //arrange
        List<String> expected = requestBodyList;

        //act
        List<String> actual = UserType.requiredUserValues("body");

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should return pathParameterList when `pathParameters` is the requestType")
    public void requiredUserValuesTest_returnsPathParametersList() {
        //arrange
        List<String> expected = pathParameterList;

        //act
        List<String> actual = UserType.requiredUserValues("pathParameters");

        //assert
        assertEquals(expected, actual);
    }

}
