package com.chairforce.utilities;

import com.chairforce.dao.UserDynamoDbDao;
import com.chairforce.entities.User;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserUtilTest {

    @Mock UserDynamoDbDao userDynamoDbDao;
    UserUtil userUtil;
    Gson gson = new Gson();

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        userUtil = new UserUtil(userDynamoDbDao);
    }

    @Test
    @DisplayName("Test create user from json")
    public void testCreateUserFromJson() {
        // Arrange
        String jsonString = "{\"userId\":\"1\",\"firstName\":\"testUser\",\"lastName\":\"testUser\",\"email\":\"testUser\",\"age\":\"10\" }";
        User user = gson.fromJson(jsonString, User.class);
        Mockito.when(userDynamoDbDao.createUser(user)).thenReturn(user);

        // Act
        User createdUser = userUtil.createUserFromJson(jsonString);

        //Assert
        assertEquals(user, createdUser);
    }

    @Test
    @DisplayName("Test get user from json")
    public void testGetUserFromJson() {
        // Assert
        String jsonString = "{\"userId\":\"1\"}";
        User user = gson.fromJson(jsonString, User.class);
        Mockito.when(userDynamoDbDao.getUserById("1")).thenReturn(Optional.of(user));

        // Act
        Optional<User> returnedUser = userUtil.getUserFromJson(jsonString);

        // Assert
        assertEquals(Optional.of(user), returnedUser);
    }

    @Test
    @DisplayName("Test delete user from json")
    public void testDeleteUserFromJson() {
        // Arrange
        String jsonString = "{\"userId\":\"1\"}";
        User user = gson.fromJson(jsonString, User.class);
        Mockito.when(userDynamoDbDao.deleteUserById("1")).thenReturn(Optional.of(user));

        //Act
        Optional<User> deletedUser = userUtil.deleteUserFromJson(jsonString);

        // Assert
        assertEquals(Optional.of(user), deletedUser);
    }
}
