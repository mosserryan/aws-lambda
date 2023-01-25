package com.chairforce.dagger;

import com.chairforce.utilities.UserUtil;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DynamoDBAccessModule.class})
public interface Config {
    UserUtil userUtil();
}


