package com.chairforce.utilities;

import com.google.gson.JsonObject;

import javax.inject.Singleton;

@Singleton
public class LambdaStatus {
    private static LambdaStatus INSTANCE;
    private JsonObject response;
    private String statusMsg;

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

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public JsonObject getRequestObj() {
        return this.response;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }

}
