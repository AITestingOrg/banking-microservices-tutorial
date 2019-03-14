package com.ultimatesoftware.banking.customers.isolated.mocked;

import com.mongodb.client.result.DeleteResult;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import java.util.List;
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest()
public class CustomersIntegrationTest {
    @Inject
    @Client("/api/v1/customers")
    RxHttpClient client;

    private final ObjectId id1 = ObjectId.get();
    private final ObjectId id2 = ObjectId.get();
    private final Customer customer1 = new Customer(id1, "FirstName1", "LastName1");
    private final Customer customer2 = new Customer(id2, "FirstName2", "LastName2");

    private void addCustomer(Customer customer) {
        client.toBlocking().retrieve(HttpRequest.POST("", customer));
    }

    // Given Customers Exist
    @Test
    public void whenQueryingACustomer_thenReturnThatCustomer() {
        // Arrange
        addCustomer(customer1);

        // Act
        Customer customer = client.toBlocking().retrieve(HttpRequest.GET("/" + customer1.getHexId()), Customer.class);

        // Assert
        assertEquals(customer.getFirstName(), customer1.getFirstName());
        assertEquals(customer.getLastName(), customer1.getLastName());
        assertEquals(customer.getHexId(), customer1.getHexId());
    }

    @Test
    public void whenQueryingAllCustomers_thenReturnAllCustomers() {
        // Arrange
        addCustomer(customer1);
        addCustomer(customer2);

        // Act
        List<Customer> customersFound = (List<Customer>) client.toBlocking().retrieve(HttpRequest.GET(""), List.class);

        // Assert
        assertEquals(2, customersFound.size());
    }

    @Test
    public void whenDeletingTheCustomer_thenTheCustomerNoLongerExists() {
        // Arrange
        addCustomer(customer1);

        // Act
        DeleteResult deleteResult = client.toBlocking().retrieve(HttpRequest.DELETE("/" + customer1.getHexId()), DeleteResult.class);

        // Assert
        assertEquals(deleteResult.getDeletedCount(), 1);
    }
    /*
    @Test
    public void whenUpdatingACustomer_thenTheCustomerIsUpdated() {
        // Arrange
        Customer newCustomer = new Customer(id1, "Test", "Joe");

        // Act
        customerController.updateCustomer(id1, newCustomer);

        // Assert
        Optional<Customer> customerFound = customerRepository.findOne(Example.of(newCustomer));
        assertEquals("Test", customerFound.get().getFirstName());
        assertEquals("Joe", customerFound.get().getLastName());
    }

    @Test
    public void whenCreatingACustomer_thenTheCustomerExists() {
        // Arrange
        Customer newCustomer = new Customer("id", "Hello", "World");

        // Act
        String id = customerController.createCustomer(new Customer("id", "Hello", "World"));

        // Assert
        Optional<Customer> customerFound = customerRepository.findOne(Example.of(newCustomer));
        assertEquals("Hello", customerFound.get().getFirstName());
        assertEquals("World", customerFound.get().getLastName());
    }

    // Given no customers exist.

    @Test
    public void whenQueryingACustomer_thenQueryShouldReturnA404() {
        // Arrange

        // Act
        ResponseEntity response = customerController.getCustomer("1");

        // Assert
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void whenQueryAllCustomers_thenGetAllShouldReturnNoCustomers() {
        // Arrange
        customerRepository.deleteAll();

        // Act
        List<Customer> customersFound = customerController.getCustomers();

        // Assert
        assertEquals(0, customersFound.size());
    }

    @Test
    public void whenDeletingTheCustomer_then404IsReturned() {
        // Arrange

        // Act
        ResponseEntity response = customerController.deleteCustomers("1");

        // Assert
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void whenUpdatingACustomer_thenA404IsReturned() {
        // Arrange

        // Act
        ResponseEntity response = customerController.updateCustomer("1", new Customer("1", "Test", "Joe"));

        // Assert
        assertEquals(404, response.getStatusCode().value());
    }
    */
}
