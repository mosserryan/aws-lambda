package com.chairforce.utilities;

import com.chairforce.entities.UserType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> This is the javadoc for the class RequestObjectValidator. The RequestObjectValidator is a public abstract class
 * that is used to validate a JsonObject request. </p>
 *
 * <p> It has one public static method called validateRequest()  which takes a {@link JsonObject}
 * as a parameter and returns a boolean. </p>
 *
 * @author mosserryan
 */
public abstract class RequestObjectValidator {

    private static final String QUERY_STRING_PARAMETERS = "queryStringParameters";
    private static final String PATH_PARAMETERS = "pathParameters";
    private static final String REQUEST_BODY = "body";

    private static List<String> requiredKeyValues;
    private static JsonObject requestObj;

    private static final LambdaStatus lambdaStatus = LambdaStatus.getInstance();

    private RequestObjectValidator() {

    }

    /**
     * <p> A public static method used to validate requests. Takes {@link JsonObject} as a parameter and returns
     * a boolean. </p>
     *
     * <p> The method starts by assigning the jsonObject parameter to the requestObj field.
     * Then it checks if the request has a valid request type, valid keys, and valid values.</p>
     * <p>This method throws no exception.</p>
     *
     * @param jsonObject request object coming in from lambda.
     *
     * @return If all the checks pass, it sets the {@code requestObj} in the {@link LambdaStatus} instance and
     * returns {@code true}. Otherwise, it returns {@code false}.
     *
     * @author mosserryan
     */
    public static boolean validateRequest(JsonObject jsonObject) {
        requestObj = jsonObject;
        if (hasValidRequestType() && hasValidKeys() && hasValidValues()) {
            lambdaStatus.setRequestObj(requestObj);
            return true;
        }
        lambdaStatus.setRequestObj(requestObj);
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
        lambdaStatus.addResponseBody("errorMessage", "Invalid request of " + requestObj + ", must send a valid request pattern.");
        return false;
    }

    private static boolean hasValidKeys() {
        Set<String> keys = requestObj.keySet();
        for (String value : requiredKeyValues) {
            if (!keys.contains(value)) {
                lambdaStatus.addResponseBody("errorMessage", "Expected key of `" + value + "` is missing.");
                return false;
            }
        }
        return true;
    }

    private static boolean hasValidValues() {
        Map<String, JsonElement> values = requestObj.asMap();
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            if (entry.getValue().isJsonNull() || entry.getValue().getAsString().isBlank()) {
                lambdaStatus.addResponseBody("errorMessage", "Key value of `" + entry.getKey() + "` cannot be null or empty.");
                return false;
            }
        }
        return true;
    }

}
