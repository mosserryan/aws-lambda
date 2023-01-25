package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.chairforce.dagger.DaggerConfig;
import com.chairforce.entities.User;
import com.chairforce.utilities.RequestObjectValidator;
import com.chairforce.utilities.LambdaStatus;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.util.Optional;

public class GetUserLambda implements RequestStreamHandler {

    private final static LambdaStatus lambdaStatus = LambdaStatus.getInstance();
    private final UserUtil userUtil;

    public GetUserLambda() {
        userUtil = DaggerConfig.create().userUtil();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        LambdaLogger lambdaLogger = context.getLogger();
        lambdaStatus.setLambdaLogger(lambdaLogger);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader;
        JsonObject jsonObject;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            lambdaLogger.log(e.getMessage());
            return;
        }

        if (!RequestObjectValidator.validateRequest(jsonObject)) {
            return;
        }

        Optional<User> user = userUtil.getUserFromJson(lambdaStatus.getRequestObjAsString());
        JsonObject responseBody = new JsonObject();
        if (user.isEmpty()) {
            lambdaStatus.log("Could not find specified user, with supplied input:");
            responseBody.addProperty("statusCode", "404");
        } else {
            lambdaStatus.log("Successfully found the following user:" + user.get());
            responseBody.addProperty("statusCode", "200");
        }

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("body", responseBody.toString());

        writer.write(responseObject.toString());
        writer.close();
        reader.close();

    }

}
