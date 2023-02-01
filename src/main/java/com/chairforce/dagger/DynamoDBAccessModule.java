package com.chairforce.dagger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.chairforce.dao.UserDynamoDbDao;
import com.chairforce.utilities.UserUtil;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DynamoDBAccessModule {

    @Provides
    @Singleton
    public UserUtil getDynamoDbDao(UserDynamoDbDao userDynamoDbDao) {
        return new UserUtil(userDynamoDbDao);
    }

    @Provides
    @Singleton
    public UserDynamoDbDao getUserDataAccessor(DynamoDBMapper dynamoDBMapper) {
        return new UserDynamoDbDao(dynamoDBMapper);
    }

    @Provides
    @Singleton
    public DynamoDBMapper getUserDataDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("Users"))
                .build();
        return new DynamoDBMapper(amazonDynamoDB, mapperConfig);
    }

    @Provides
    @Singleton
    public AmazonDynamoDB getAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }
}

