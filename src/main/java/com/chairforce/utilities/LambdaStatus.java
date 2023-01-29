package com.chairforce.utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;

@Singleton
@Getter
@Setter
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

    public String getRequestAsString() {
        return gson.toJson(this.request);
    }

    public void setLambdaLogger(LambdaLogger lambdaLogger) {
        this.lambdaLogger = lambdaLogger;
    }

    public void log(String message) {
        lambdaLogger.log(message);
    }
}
