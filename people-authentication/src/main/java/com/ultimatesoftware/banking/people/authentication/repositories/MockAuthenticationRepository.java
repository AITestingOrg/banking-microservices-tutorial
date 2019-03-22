package com.ultimatesoftware.banking.people.authentication.repositories;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.repository.MockRepository;
import com.ultimatesoftware.banking.people.authentication.models.Authentication;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Primary
@Singleton
@Requires(env = ConfigurationConstants.EXTERNAL_MOCKS)
public class MockAuthenticationRepository extends MockRepository<Authentication> {

    public MockAuthenticationRepository() {
        entities = new ArrayList<>();
        entities.add(new Authentication(new ObjectId("507f1f77bcf86cd799439011"), "test@example.com", "Thisis@test"));
        entities.add(new Authentication(new ObjectId("507f191e810c19729de860ea"), "test2@example.com", "Thisis@test"));
        entities.add(new Authentication(new ObjectId("507f191e810c19729de860ee"), "test3@example.com", "Thisis3@test"));
    }
}
