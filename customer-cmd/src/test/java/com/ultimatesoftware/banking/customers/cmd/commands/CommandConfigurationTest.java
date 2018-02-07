package com.ultimatesoftware.banking.customerscmd.commands;

import com.rabbitmq.client.Command;
import com.ultimatesoftware.banking.customer.common.events.CustomerCreatedEvent;
import com.ultimatesoftware.banking.customer.common.events.CustomerDeletedEvent;
import com.ultimatesoftware.banking.customer.common.events.CustomerUpdatedEvent;
import com.ultimatesoftware.banking.customerscmd.domain.aggregates.Customer;
import com.ultimatesoftware.banking.customerscmd.domain.commands.CreateCustomerCommand;
import com.ultimatesoftware.banking.customerscmd.domain.commands.DeleteCustomerCommand;
import com.ultimatesoftware.banking.customerscmd.domain.commands.UpdateCustomerCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandConfigurationTest {

    private FixtureConfiguration<Customer> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(Customer.class);
    }

    @Test
    public void CreateCustomer_ValidConfig() {
        String firstName = "Jane";
        String lastName = "Doe";
        CreateCustomerCommand command = new CreateCustomerCommand(firstName, lastName);
        fixture.given()
                .when(command)
                .expectEvents(new CustomerCreatedEvent(command.getId(), firstName, lastName));
    }

    @Test
    public void UpdateCustomer_ValidConfig() {
        String firstName = "Jane";
        String lastName = "Doe";
        String updateName = "Bill";
        CreateCustomerCommand createCommand = new CreateCustomerCommand(firstName, lastName);
        UpdateCustomerCommand command = new UpdateCustomerCommand(createCommand.getId(), updateName, lastName);
        fixture.givenCommands(createCommand)
                .when(command)
                .expectEvents(new CustomerUpdatedEvent(command.getId(), updateName, lastName));
    }

    @Test
    public void DeleteCustomer_ValidConfig() {
        String firstName = "Jane";
        String lastName = "Doe";
        CreateCustomerCommand createCommand = new CreateCustomerCommand(firstName, lastName);
        fixture.givenCommands(createCommand)
                .when(new DeleteCustomerCommand(createCommand.getId()))
                .expectEvents(new CustomerDeletedEvent(createCommand.getId()));
    }
}
