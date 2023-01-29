package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.chairforce.dagger.DaggerConfig;
import com.chairforce.entities.User;
import com.chairforce.utilities.RequestObjectValidator;
import com.chairforce.utilities.LambdaStatus;
import com.chairforce.utilities.ResponseBuilder;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.util.Optional;

public class GetUserLambda implements RequestStreamHandler {

    private final static LambdaStatus lambdaStatus = LambdaStatus.getInstance();
    private final UserUtil userUtil;
    private JsonObject response;

    public GetUserLambda() {
        userUtil = DaggerConfig.create().userUtil();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaStatus.setLambdaLogger(lambdaLogger);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader;
        JsonObject request;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            request = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            lambdaLogger.log(e.getMessage());
            return;
        }

        // TODO Subject to change, see TODO in RequestObjectValidator class
        if (!RequestObjectValidator.validateRequest(request)) {
            lambdaStatus.log("Unable to validate invalid request of: " + lambdaStatus.getRequestAsString());
            response = new ResponseBuilder()
                    .setStatusCode(400)
                    .setErrorBody("Invalid request")
                    .build();
            writer.write(response.toString());
            writer.close();
            reader.close();
            return;
        }

        Optional<User> user = userUtil.getUserFromJson(lambdaStatus.getRequestAsString());
        if (user.isEmpty()) {
            lambdaStatus.log("Could not find specified User, with supplied input of: " + lambdaStatus.getRequestAsString());
            response = new ResponseBuilder()
                    .setStatusCode(404)
                    .setErrorBody("Resource not found")
                    .build();
        } else {
            String userJson = userUtil.convertUserToJson(user.get());
            lambdaStatus.log("Successfully found the following User: " + userJson);
            response = new ResponseBuilder()
                    .setStatusCode(200)
                    .setSuccessBody(userJson)
                    .build();
        }

        lambdaStatus.log(response.toString());

        writer.write(response.toString());
        writer.close();
        reader.close();
    }

}
