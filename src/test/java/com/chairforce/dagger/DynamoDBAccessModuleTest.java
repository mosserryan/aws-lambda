package com.chairforce.dagger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.chairforce.dao.UserDynamoDbDao;
import com.chairforce.utilities.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;

public class DynamoDBAccessModuleTest {

    @Mock AmazonDynamoDB amazonDynamoDB;
    @Mock DynamoDBMapper dynamoDBMapper;
    @Mock UserDynamoDbDao userDynamoDbDao;
    private DynamoDBAccessModule dynamoDBAccessModule;

    @BeforeEach
    public void setup() {
        dynamoDBAccessModule = new DynamoDBAccessModule();
    }

    @Test
    @DisplayName("Should return a DynamoDBMapper object")
    public void getUserDataDynamoDBMapper_ShouldReturnDynamoDBMapperObject() {
        DynamoDBMapper dynamoDBMapper = dynamoDBAccessModule.getUserDataDynamoDBMapper(amazonDynamoDB);
        assertNotNull(dynamoDBMapper);
    }

    @Test
    @DisplayName("Should return a UserDynamoDbDao")
    public void getUserDataAccessor_ShouldReturnUserDynamoDbDao() {
        UserDynamoDbDao userDynamoDbDao = dynamoDBAccessModule.getUserDataAccessor(dynamoDBMapper);
        assertNotNull(userDynamoDbDao);
    }

    @Test
    @DisplayName("Should return a UserUtil object")
    public void getDynamoDbDao_ShouldReturnUserUtilObject() {
        UserUtil userUtil = dynamoDBAccessModule.getDynamoDbDao(userDynamoDbDao);
        assertNotNull(userUtil);
    }

}
