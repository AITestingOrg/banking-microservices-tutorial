package com.ultimatesoftware.banking.customers.service.isolation;

import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest()
public class CustomersEndpointsTest {
    @Inject
    @Client("/api/v1/customers")
    RxHttpClient client;

    @Inject
    Repository<Customer> mongoRepository;

    private final ObjectId id1 = ObjectId.get();
    private final ObjectId id2 = ObjectId.get();
    private final Customer customer1 = new Customer(id1, "FirstName1", "LastName1");
    private final Customer customer2 = new Customer(id2, "FirstName2", "LastName2");


    @BeforeEach
    public void beforeEach() {
        List<String> accountIds = mongoRepository.findMany().blockingGet()
            .stream()
            .map(account -> account.getHexId()).collect(Collectors.toList());
        accountIds.forEach(id -> mongoRepository.deleteOne(id).blockingGet());
    }

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
        String result = client.toBlocking().retrieve(HttpRequest.DELETE(customer1.getHexId()));

        // Assert
        assertEquals("{\"deletedCount\":1}", result);
    }

    @Test
    public void whenUpdatingACustomer_thenTheCustomerIsUpdated() {
        // Arrange
        addCustomer(customer1);
        Customer updated = new Customer(customer1.getId(), "Jane", "Doe");

        // Act
        String result = client.toBlocking().retrieve(HttpRequest.PUT(updated.getHexId(), updated));

        // Assert
        assertEquals("{\"matchedCount\":1,\"modifiedCount\":1,\"modifiedCountAvailable\":true}", result);
    }

    @Test
    public void whenCreatingACustomer_thenTheCustomerExists() {
        // Arrange

        // Act
        Customer customer = client.toBlocking().retrieve(HttpRequest.POST("", "{\"firstName\":\"John\", \"lastName\":\"Doe\"}"), Customer.class);

        // Assert
        assertEquals(customer.getFirstName(), "John");
        assertEquals(customer.getLastName(), "Doe");
        assertNotNull(customer.getHexId());
    }

    // Given no customers exist.

    @Test
    public void whenQueryingACustomer_thenQueryShouldReturnA404() {
        // Arrange

        // Act
        HttpClientResponseException e = Assertions.assertThrows(HttpClientResponseException.class, () -> {
                Customer customer =
                    client.toBlocking().retrieve(HttpRequest.GET("/" + ObjectId.get()), Customer.class);
            });

        // Assert
        assertTrue(e.getStatus().getCode() == 404);
    }

    @Test
    public void whenDeletingACustomer_thenDeleteShouldReturnA404() {
        // Arrange

        // Act
        String result = client.toBlocking().retrieve(HttpRequest.DELETE("/" + ObjectId.get()));


        // Assert
        assertEquals("{\"deletedCount\":0}", result);
    }

    @Test
    public void whenUpdatingACustomer_thenA404IsReturned() {
        // Arrange

        // Act
        String result = client.toBlocking().retrieve(HttpRequest.PUT("/" + ObjectId.get(), customer1));

        // Assert
        assertEquals("{\"matchedCount\":0,\"modifiedCount\":0,\"modifiedCountAvailable\":true}", result);
    }
}
