package ultimatesoftware.banking.customers.domain.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class CreateCustomerCommand {
    @TargetAggregateIdentifier
    private UUID id;
    private String firstName = "";
    private String lastName = "";

    public CreateCustomerCommand(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
