package com.chairforce.utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Singleton;

@Singleton
public class LambdaStatus {
    private static LambdaStatus INSTANCE;
    private LambdaLogger lambdaLogger;
    private JsonObject request = new JsonObject();
    private final JsonObject response = new JsonObject();
    private final Gson gson = new Gson();

    private LambdaStatus() {
    }

    public static LambdaStatus getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LambdaStatus();
        }
        return INSTANCE;
    }

    public void setRequestObj(JsonObject request) {
        this.request = request;
    }

    public JsonObject getRequestObj() {
        return this.request;
    }

    public String getRequestObjAsString() {
        return gson.toJson(this.request);
    }

    public void addResponseProperty(String key, int value) {
        this.response.addProperty(key, value);
    }

    public void addResponseBody(String userJson) {
        JsonObject body = new JsonObject();
        body.addProperty("user", userJson);
        this.response.addProperty("body", gson.toJson(body).replaceAll("\\\\", ""));

    }

    public String getResponseObjAsString() {
        return gson.toJson(this.response);
    }

    public void setLambdaLogger(LambdaLogger lambdaLogger) {
        this.lambdaLogger = lambdaLogger;
    }

    public void log(String message) {
        lambdaLogger.log(message);
    }

}
