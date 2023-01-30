package com.chairforce.utilities;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import javax.inject.Singleton;

@Singleton
public class LambdaStatus {
    private static LambdaStatus INSTANCE;
    private LambdaLogger lambdaLogger;

    private LambdaStatus() {
    }

    public static LambdaStatus getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LambdaStatus();
        }
        return INSTANCE;
    }

    public void log(String message) {
        lambdaLogger.log(message);
    }

    public LambdaLogger getLambdaLogger() {
        return this.lambdaLogger;
    }

    public void setLambdaLogger(LambdaLogger lambdaLogger) {
        this.lambdaLogger = lambdaLogger;
    }

}
