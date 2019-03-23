package com.ultimatesoftware.banking.people.cmd.aggregates;

import com.ultimatesoftware.banking.people.cmd.commands.CreatePersonCommand;
import com.ultimatesoftware.banking.people.cmd.commands.ResetPasswordCommand;
import com.ultimatesoftware.banking.people.cmd.commands.UpdateDetailsCommand;
import com.ultimatesoftware.banking.people.cmd.exceptions.InvalidPasswordException;
import com.ultimatesoftware.banking.people.cmd.rules.PasswordRules;
import com.ultimatesoftware.banking.people.cmd.rules.PasswordRulesImpl;
import com.ultimatesoftware.banking.people.events.DetailsUpdatedEvent;
import com.ultimatesoftware.banking.people.events.PasswordResetEvent;
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

    private PasswordRules passwordRules = new PasswordRulesImpl();

    @CommandHandler
    public Person(CreatePersonCommand command) throws InvalidPasswordException {
        passwordRules.passwordMeetsRules(command.getPerson().getPassword());
        applyEvent(PersonCreatedEvent.builder()
            .id(command.getId())
            .email(command.getPerson().getEmail())
            .password(command.getPerson().getPassword())
            .firstName(command.getPerson().getFirstName())
            .lastName(command.getPerson().getLastName())
            .build());
    }

    @CommandHandler
    public void handle(ResetPasswordCommand command) throws InvalidPasswordException {
        passwordRules.passwordMeetsRules(command.getAuthenticationDto().getPassword());
        applyEvent(PasswordResetEvent.builder()
            .id(command.getId().toHexString())
            .email(command.getAuthenticationDto().getEmail())
            .password(command.getAuthenticationDto().getPassword())
            .build());
    }

    @CommandHandler
    public void handle(UpdateDetailsCommand command) {
        applyEvent(DetailsUpdatedEvent.builder()
            .id(command.getId().toHexString())
            .firstName(command.getPersonDetailsDto().getFirstName())
            .lastName(command.getPersonDetailsDto().getLastName())
            .build());
    }

    @EventSourcingHandler
    public void on(PersonCreatedEvent event) {
        id = new ObjectId(event.getId());
        email = event.getEmail();
        password = event.getPassword();
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }

    @EventSourcingHandler
    public void on(ResetPasswordCommand event) {
        password = event.getAuthenticationDto().getPassword();
    }

    @EventSourcingHandler
    public void on(DetailsUpdatedEvent event) {
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }

    private void applyEvent(PersonEvent event) {
        apply(event);
    }
}
