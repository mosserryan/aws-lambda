package com.chairforce.utilities;

import com.chairforce.entities.UserType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class RequestObjectValidator {

    private static final String QUERY_STRING_PARAMETERS = "queryStringParameters";
    private static final String PATH_PARAMETERS = "pathParameters";
    private static final String REQUEST_BODY = "body";

    private static List<String> requiredKeyValues;
    private static JsonObject requestObj;

    private static final LambdaStatus lambdaStatus = LambdaStatus.getInstance();

    private RequestObjectValidator() {

    }

    public static boolean validateRequest(JsonObject jsonObject) {
        requestObj = jsonObject;
        if (hasValidRequestType() && hasValidKeys() && hasValidValues()) {
            lambdaStatus.setRequestObj(requestObj);
            return true;
        }
        return false;
    }

    private static boolean hasValidRequestType() {
        String[] keys = {PATH_PARAMETERS, QUERY_STRING_PARAMETERS, REQUEST_BODY};
        for (String key : keys) {
            if (requestObj.has(key)) {
                requestObj = requestObj.get(key).getAsJsonObject();
                requiredKeyValues = UserType.requiredUserValues(key);
                return true;
            }
        }
        lambdaStatus.setStatusMsg("Must send a valid request pattern.");
        return false;
    }

    private static boolean hasValidKeys() {
        Set<String> keys = requestObj.keySet();
        for (String value : requiredKeyValues) {
            if (!keys.contains(value)) {
                lambdaStatus.setStatusMsg("Expected key of `" + value + "` is missing.");
                return false;
            }
        }
        return true;
    }

    private static boolean hasValidValues() {
        Map<String, JsonElement> values = requestObj.asMap();
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            if (entry.getValue().isJsonNull() || entry.getValue().getAsString().isBlank()) {
                lambdaStatus.setStatusMsg("Key value of `" + entry.getKey() + "` cannot be null or empty.");
                return false;
            }
        }
        return true;
    }

}