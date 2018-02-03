package ultimatesoftware.banking.customers.commands;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ultimatesoftware.banking.customers.domain.commands.CreateCustomerCommand;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandConfigurationTest {

    private FixtureConfiguration<CustomerAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<CustomerAggregate>(CustomerAggregate.class);
    }

    @Test
    public void CreateCustomer_ValidConfig() {
        String firstName = "Jane";
        String lastName = "Doe";
        UUID id = UUID.randomUUID();
        fixture.given()
                .when(new CreateCustomerCommand(id, firstName, lastName))
                .expectEvents(new CustomerCreatedEvent(id, firstName, lastName));
    }
}
