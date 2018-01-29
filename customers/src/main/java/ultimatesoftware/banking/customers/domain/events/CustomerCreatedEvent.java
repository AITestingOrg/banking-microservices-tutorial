package ultimatesoftware.banking.customers.domain.events;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CustomerCreatedEvent {
    @NotNull
    private UUID id;

    private String firstName = "";
    private String lastName = "";

    public CustomerCreatedEvent(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UUID getId() {
        return id;
    }
}
