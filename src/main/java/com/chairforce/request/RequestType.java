package com.chairforce.request;

import java.util.List;

public enum RequestType {

    PATH_PARAMETERS("pathParameters"),
    QUERY_STRING_PARAMETERS("queryStringParameters"),
    REQUEST_BODY("body");

    public final String value;

    RequestType(String label) {
        this.value = label;
    }

    public static List<String> getStringValues() {
        return List.of(PATH_PARAMETERS.value, QUERY_STRING_PARAMETERS.value, REQUEST_BODY.value);
    }
}
