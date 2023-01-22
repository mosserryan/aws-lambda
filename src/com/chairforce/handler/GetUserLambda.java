package com.chairforce.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;


public class GetUserLambda implements RequestStreamHandler {

    private static final Logger log = LogManager.getLogger(GetUserLambda.class);

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        System.out.println("Hello World!");

    }

}
