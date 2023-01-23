package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.chairforce.utilities.RequestObjectValidator;
import com.chairforce.utilities.LambdaStatus;
import com.chairforce.utilities.UserUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.inject.Inject;
import java.io.*;


public class GetUserLambda implements RequestStreamHandler {

    @Inject UserUtil userUtil;

    private static final Logger log = LogManager.getLogger(GetUserLambda.class);
    private final static LambdaStatus lambdaStatus = LambdaStatus.getInstance();


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        OutputStreamWriter output = new OutputStreamWriter(outputStream);

        if (!RequestObjectValidator.validateRequest(jsonObject)) {
            return;
        }

        System.out.println(lambdaStatus.getRequestObj());

         System.out.println("It's not false!");

    }

}
