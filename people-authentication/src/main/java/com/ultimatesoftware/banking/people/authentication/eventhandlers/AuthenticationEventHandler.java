package com.ultimatesoftware.banking.people.authentication.eventhandlers;

import com.ultimatesoftware.banking.api.operations.AxonEventHandler;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.people.authentication.models.Authentication;
import com.ultimatesoftware.banking.people.events.PasswordResetEvent;
import com.ultimatesoftware.banking.people.events.PersonCreatedEvent;
import javax.inject.Singleton;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.eventhandling.EventHandler;
import org.bson.types.ObjectId;

@Singleton
public class AuthenticationEventHandler extends AxonEventHandler {
    private final Repository<Authentication> mongoRepository;

    public AuthenticationEventHandler(Repository<Authentication> mongoRepository,
        AxonServerConfiguration axonServerConfiguration) {
        super(axonServerConfiguration);
        this.mongoRepository = mongoRepository;
    }

    @EventHandler
    public void on(PersonCreatedEvent event) {
        mongoRepository.add(new Authentication(new ObjectId(event.getId()), event.getEmail(), event.getPassword())).blockingGet();
    }

    @EventHandler
    public void on(PasswordResetEvent event) {
        mongoRepository.replaceOne(event.getId(), new Authentication(new ObjectId(event.getId()), event.getEmail(), event.getPassword())).blockingGet();
    }
}
