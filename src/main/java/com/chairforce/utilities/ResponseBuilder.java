package com.chairforce.utilities;

import com.google.gson.*;

public class ResponseBuilder {

    private final JsonObject response;

    public ResponseBuilder() {
        response = new JsonObject();
    }

    public ResponseBuilder setStatusCode(int statusCode) {
        response.addProperty("statusCode", statusCode);
        return this;
    }

    public ResponseBuilder setSuccessBody(String user) {
        JsonElement bodyContent = JsonParser.parseString(user);
        JsonObject body = new JsonObject();
        body.add("user", bodyContent);
        response.addProperty("body", body.toString());
        return this;
    }

    public ResponseBuilder setErrorBody(String errorMsg) {
        response.addProperty("body", errorMsg);
        return this;
    }

    public JsonObject build() {
        return response;
    }
}
