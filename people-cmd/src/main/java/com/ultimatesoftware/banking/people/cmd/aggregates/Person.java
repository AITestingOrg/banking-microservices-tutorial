package com.ultimatesoftware.banking.people.cmd.aggregates;

import com.ultimatesoftware.banking.people.cmd.commands.CreatePersonCommand;
import com.ultimatesoftware.banking.people.events.PersonCreatedEvent;
import com.ultimatesoftware.banking.people.events.PersonEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.bson.types.ObjectId;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Getter
@AllArgsConstructor
public class Person {
    @AggregateIdentifier
    private ObjectId id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @CommandHandler
    public Person(CreatePersonCommand command) {
        applyEvent(new PersonCreatedEvent(command.getId(), command.getPerson()));
    }

    @EventSourcingHandler
    public void on(PersonCreatedEvent event) {
        id = new ObjectId(event.getId());
        email = event.getPerson().getEmail();
        password = event.getPerson().getPassword();
        firstName = event.getPerson().getFirstName();
        lastName = event.getPerson().getLastName();
    }

    private void applyEvent(PersonEvent event) {
        apply(event);
    }
}
