package com.chairforce.utilities;

import com.chairforce.dao.UserDynamoDbDao;
import com.chairforce.entities.User;
import com.chairforce.entities.UserType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.Optional;

public class UserUtil {

    private final Gson gson = new Gson();
    private final UserDynamoDbDao userDynamoDbDao;

    public UserUtil(UserDynamoDbDao userDynamoDbDao) {
        this.userDynamoDbDao = userDynamoDbDao;
    }

    public User createUserFromJson(String jsonString) {
        User user = gson.fromJson(jsonString, User.class);
        return this.userDynamoDbDao.createUser(user);
    }

    public Optional<User> getUserFromJson(String jsonString) {
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        String userId = jsonElement.getAsJsonObject().get(UserType.USER_ID.value).getAsString();
        return this.userDynamoDbDao.getUserById(userId);
    }

    public Optional<User> deleteUserFromJson(String jsonString) {
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        String userId = jsonElement.getAsJsonObject().get(UserType.USER_ID.value).getAsString();
        return this.userDynamoDbDao.deleteUserById(userId);
    }

    public String convertUserToJson(User user) {
       return gson.toJson(user);
    }

}
