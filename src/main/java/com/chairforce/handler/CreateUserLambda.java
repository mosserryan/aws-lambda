package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.chairforce.dagger.DaggerConfig;
import com.chairforce.entities.User;
import com.chairforce.request.RequestWrapper;
import com.chairforce.response.ResponseBuilder;
import com.chairforce.utilities.LambdaStatus;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.*;

public class CreateUserLambda implements RequestStreamHandler {

    private final LambdaStatus lambdaStatus = LambdaStatus.getInstance();
    private final UserUtil userUtil;

    public CreateUserLambda() {
        userUtil = DaggerConfig.create().userUtil();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaStatus.setLambdaLogger(lambdaLogger);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        BufferedReader reader;
        JsonObject requestObj;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            requestObj = (JsonObject) JsonParser.parseReader(reader);
        } catch (JsonParseException e) {
            lambdaLogger.log(e.getMessage());
            return;
        }

        RequestWrapper request = new RequestWrapper(requestObj);
        JsonObject response;
        if (!request.isValidRequest()) {
            lambdaStatus.log("Unable to validate invalid request of: " + request.getRequestAsJson());
            response = new ResponseBuilder()
                    .setStatusCode(400)
                    .setErrorBody("Invalid request")
                    .build();
            writer.write(response.toString());
            writer.close();
            reader.close();
            return;
        }

        User user = userUtil.createUserFromJson(request.getRequestAsJson());
            String userJson = userUtil.convertUserToJson(user);
            lambdaStatus.log("Successfully created the following User: " + userJson);
            response = new ResponseBuilder()
                    .setStatusCode(200)
                    .setSuccessBody(userJson)
                    .build();

        writer.write(response.toString());
        writer.close();
        reader.close();
    }

}
