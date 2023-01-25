package com.chairforce.utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Singleton;

@Singleton
public class LambdaStatus {
    private static LambdaStatus INSTANCE;
    private JsonObject response;
    private LambdaLogger lambdaLogger;
    Gson gson = new Gson();

    private LambdaStatus() {
    }

    public static LambdaStatus getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LambdaStatus();
        }
        return INSTANCE;
    }

    public void setRequestObj(JsonObject response) {
        this.response = response;
    }

    public JsonObject getRequestObj() {
        return this.response;
    }

    public String getRequestObjAsString() {
        return gson.toJson(this.response);
    }

    public void setLambdaLogger(LambdaLogger lambdaLogger) {
        this.lambdaLogger = lambdaLogger;
    }

    public void log(String message) {
        lambdaLogger.log(message);
    }

}
