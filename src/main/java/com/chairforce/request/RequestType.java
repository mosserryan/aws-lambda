package com.chairforce.request;

import java.util.List;

public enum RequestType {

    PATH_PARAMETERS("pathParameters"),
    QUERY_STRING_PARAMETERS("queryStringParameters"),
    BODY("body");

    public final String value;

    RequestType(String label) {
        this.value = label;
    }

    public static List<String> getStringValues() {
        return List.of(PATH_PARAMETERS.value, QUERY_STRING_PARAMETERS.value, BODY.value);
    }

    public static List<String> getStringValues(String httpMethod) {
        switch(httpMethod) {
            case "POST":
            case "PUT":
                return List.of(BODY.value);
            case "DELETE":
            case "GET":
                return List.of(PATH_PARAMETERS.value, QUERY_STRING_PARAMETERS.value);
        }
        return getStringValues();
    }
}
