package com.chairforce.dao;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.chairforce.entities.User;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDynamoDbDaoTest {

    private User user;
    private final Gson gson = new Gson();

    @Mock
    DynamoDBMapper dynamoDBMapper;
    @InjectMocks
    UserDynamoDbDao userDynamoDbDao;

    @BeforeEach
    public void setup() {
        String jsonString =
                "{\"firstName\":\"testUser\",\"lastName\":\"testUser\",\"email\":\"testUser\",\"age\":\"10\" }";
        user = gson.fromJson(jsonString, User.class);
    }

    @Test
    @DisplayName("Should return empty when the user is not found")
    public void deleteUserByIdWhenUserIsNotFoundThenReturnEmpty() {
        // Arrange
        when(dynamoDBMapper.load(User.class, "testUser")).thenReturn(null);

        // Act
        Optional<User> user = userDynamoDbDao.deleteUserById("testUser");

        // Assert
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Should return the user when the user is found")
    public void deleteUserByIdWhenUserIsFoundThenReturnTheUser() {
        // Arrange
        when(dynamoDBMapper.load(User.class, "testUser")).thenReturn(user);

        // Act
        Optional<User> userOptional = userDynamoDbDao.deleteUserById("testUser");

        // Assert
        assertTrue(userOptional.isPresent());
        assertEquals(userOptional.get(), user);
    }

    @Test
    @DisplayName("Should return an empty optional when the user does not exist")
    public void testGetUserByIdWhenUserDoesNotExistThenReturnEmptyOptional() {
        // Arrange
        user.setUserId("userId");
        when(dynamoDBMapper.load(User.class, "userId")).thenReturn(null);

        // Act
        Optional<User> user = userDynamoDbDao.getUserById("userId");

        // Assert
        assertEquals(Optional.empty(), user);
    }

    @Test
    @DisplayName("Should return an optional with the user when the user exists")
    public void testGetUserByIdWhenUserExistsThenReturnOptionalWithTheUser() {
        // Arrange
        user.setUserId("userId");
        when(dynamoDBMapper.load(User.class, "userId")).thenReturn(user);

        // Act
        Optional<User> userById = userDynamoDbDao.getUserById("userId");

        // Assert
        assertTrue(userById.isPresent());
        assertEquals(user, userById.get());
    }

    @Test
    @DisplayName("Should save the user")
    public void testCreateUserShouldSaveTheUser() {
        // Arrange - Act
        userDynamoDbDao.createUser(user);

        // Assert
        verify(dynamoDBMapper, times(1)).save(user);
    }
}
