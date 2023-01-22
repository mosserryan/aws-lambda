package com.chairforce.entities;

import java.util.Arrays;
import java.util.List;

public enum UserType {
    USER_ID("userId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    AGE("age"),
    EMAIL("email");

    public final String value;

    UserType(String label) {
        this.value = label;
    }

    public static List<String> requiredUserValues(String requestType) {
        switch(requestType) {
            case "pathParameters":
                return List.of(USER_ID.value);
            case "body":
                return List.of(FIRST_NAME.value, LAST_NAME.value, AGE.value, EMAIL.value);
        }
        return List.of(Arrays.toString(values()));
    }
}

