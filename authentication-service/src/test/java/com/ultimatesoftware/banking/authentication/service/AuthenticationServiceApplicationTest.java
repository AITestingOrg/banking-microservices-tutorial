package com.ultimatesoftware.banking.authentication.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Profile("test")
@SpringBootTest
public class AuthenticationServiceApplicationTest {
    @Before
    public void setup() {
        MongoClient mongo = Mockito.mock(MongoClient.class);
        MongoDatabase db = Mockito.mock(MongoDatabase.class);
        Mockito.when(mongo.getDatabase("Authorizations")).thenReturn(db);
    }
}
