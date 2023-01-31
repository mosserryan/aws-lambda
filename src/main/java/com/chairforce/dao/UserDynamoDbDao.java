package com.chairforce.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.chairforce.entities.User;
import java.util.Optional;

public class UserDynamoDbDao {

    private final DynamoDBMapper dynamoDBMapper;

    public UserDynamoDbDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public User createUser(User user) {
        dynamoDBMapper.save(user);
        return user;
    }

    public Optional<User> getUserById(String userId) {
        User user = dynamoDBMapper.load(User.class, userId);
        return Optional.ofNullable(user);
    }

    public Optional<User> deleteUserById(String userId) {
        Optional<User> targetUser = getUserById(userId);
        if (targetUser.isEmpty()) {
            return Optional.empty();
        } else {
            dynamoDBMapper.delete(targetUser.get());
            return targetUser;
        }
    }

    public Optional<User> updateUserById(String userId, User user) {
        ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue((new AttributeValue()).withS(userId));
        DynamoDBSaveExpression dynamoDBSaveExpression = (new DynamoDBSaveExpression()).withExpectedEntry("UserId", expectedAttributeValue);

        Optional<User> targetUser = getUserById(userId);
        if (targetUser.isEmpty()) {
            return Optional.empty();
        } else {
            dynamoDBMapper.save(user, dynamoDBSaveExpression);
            return getUserById(userId);
        }
    }
}
