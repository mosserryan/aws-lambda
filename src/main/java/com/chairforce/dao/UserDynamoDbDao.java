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
        Optional<User> user = getUserById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            dynamoDBMapper.delete(user.get());
            return user;
        }
    }

    public User updateUserById(String userId, User user) {
        ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue((new AttributeValue()).withS(userId));
        DynamoDBSaveExpression dynamoDBSaveExpression = (new DynamoDBSaveExpression()).withExpectedEntry("UserId", expectedAttributeValue);
        this.dynamoDBMapper.save(user, dynamoDBSaveExpression);
        return user;
    }
}
