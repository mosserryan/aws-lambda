package com.chairforce.request;

import com.chairforce.entities.UserType;
import com.chairforce.utilities.LambdaStatus;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> This is the javadoc for the <code>RequestWrapper</code> class. The <code>RequestWrapper</code> is a public class
 * that is used to wrap and validate an incoming request {@link JsonObject} from the handler class. </p>
 *
 * <p> It has a constructor that takes in {@link JsonObject} and two public methods called <code>getRequestObj()</code>
 * and <code>getRequestAsJson()</code> </p>
 *
 * @author mosserryan
 */
public class RequestWrapper {

    private List<String> requiredKeyValues;
    private JsonObject requestObj;
    private boolean isValidRequest = false;

    private final Gson gson = new Gson();

    private final LambdaStatus lambdaStatus = LambdaStatus.getInstance();

    /**
     * <p>A public constructor used to wrap requests. Upon instantiation this will check if the currently wrapped
     * {@link JsonObject} has the expected request type, keys, and values. The <code>isValidRequest</code> field will be marked
     * {@code true} if all checks pass. Otherwise, it returns {@code false}.</p>
     *
     * @param requestObj {@link JsonObject} coming in from handler.
     *
     * @author mosserryan
     */
    public RequestWrapper(JsonObject requestObj) {
        this.requestObj = requestObj;
        validateRequest();
    }

    private void validateRequest() {
        if (hasValidReqType() && hasValidKeys() && hasValidValues()) {
            lambdaStatus.log("Request has been successfully validated");
            isValidRequest = true;
        }
    }

    private boolean hasValidReqType() {
        List<String> keys =  RequestType.getStringValues();
        for (String key : keys) {
            if (requestObj.has(key)) {
                requestObj = requestObj.get(key).getAsJsonObject();
                requiredKeyValues = UserType.requiredUserValues(key);
                return true;
            }
        }
        lambdaStatus.log("Invalid request of " + requestObj + ", must send a valid request pattern.");
        return false;
    }

    private boolean hasValidKeys() {
        Set<String> keys = requestObj.keySet();
        for (String value : requiredKeyValues) {
            if (!keys.contains(value)) {
                lambdaStatus.log("Expected key of `" + value + "` is missing.");
                return false;
            }
        }
        return true;
    }

    private boolean hasValidValues() {
        Map<String, JsonElement> values = requestObj.asMap();
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            if (entry.getValue().isJsonNull() || entry.getValue().getAsString().isBlank()) {
                lambdaStatus.log("Key value of `" + entry.getKey() + "` cannot be null or empty.");
                return false;
            }
        }
        return true;
    }

    public JsonObject getRequestObj() {
        return requestObj;
    }

    public String getRequestAsJson() {
        return gson.toJson(requestObj);
    }

    public boolean isValidRequest() {
        return isValidRequest;
    }


}
