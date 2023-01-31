package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.chairforce.dagger.DaggerConfig;
import com.chairforce.entities.User;
import com.chairforce.request.RequestWrapper;
import com.chairforce.utilities.LambdaStatus;
import com.chairforce.response.ResponseBuilder;
import com.chairforce.utilities.UserUtil;
import com.google.gson.*;
import java.io.*;
import java.util.Optional;


public class GetUserLambda implements RequestStreamHandler {

    private final LambdaStatus lambdaStatus = LambdaStatus.getInstance();
    private final UserUtil userUtil;

    public GetUserLambda() {
        userUtil = DaggerConfig.create().userUtil();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaStatus.setLambdaLogger(lambdaLogger);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {

            JsonObject requestObj = JsonParser.parseReader(reader).getAsJsonObject();
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

            Optional<User> user = userUtil.getUserFromJson(request.getRequestAsJson());
            if (user.isEmpty()) {
                lambdaStatus.log("Could not find specified User, with supplied input of: " + request.getRequestAsJson());
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

            writer.write(response.toString());
        } catch (IllegalStateException | JsonSyntaxException exception) {
            lambdaStatus.log(exception.toString());
        } finally {
            reader.close();
            writer.close();
        }
    }

}
