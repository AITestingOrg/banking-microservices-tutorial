package com.ultimatesoftware.banking.person.details.tests.service.isolation;

import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
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
public class PersonDetailsEndpointsTest {
    @Inject
    @Client("/api/v1/customers")
    RxHttpClient client;

    @Inject
    Repository<PersonDetails> mongoRepository;

    private final ObjectId id1 = ObjectId.get();
    private final ObjectId id2 = ObjectId.get();
    private final PersonDetails
        personDetails1 = new PersonDetails(id1, "FirstName1", "LastName1");
    private final PersonDetails
        personDetails2 = new PersonDetails(id2, "FirstName2", "LastName2");


    @BeforeEach
    public void beforeEach() {
        List<String> accountIds = mongoRepository.findMany().blockingGet()
            .stream()
            .map(account -> account.getHexId()).collect(Collectors.toList());
        accountIds.forEach(id -> mongoRepository.deleteOne(id).blockingGet());
    }

    private void addCustomer(
        PersonDetails personDetails) {
        client.toBlocking().retrieve(HttpRequest.POST("", personDetails));
    }

    // Given Customers Exist
    @Test
    public void whenQueryingACustomer_thenReturnThatCustomer() {
        // Arrange
        addCustomer(personDetails1);

        // Act
        PersonDetails
            personDetails = client.toBlocking().retrieve(HttpRequest.GET("/" + personDetails1.getHexId()), PersonDetails.class);

        // Assert
        assertEquals(personDetails.getFirstName(), personDetails1.getFirstName());
        assertEquals(personDetails.getLastName(), personDetails1.getLastName());
        assertEquals(personDetails.getHexId(), personDetails1.getHexId());
    }

    @Test
    public void whenQueryingAllCustomers_thenReturnAllCustomers() {
        // Arrange
        addCustomer(personDetails1);
        addCustomer(personDetails2);

        // Act
        List<PersonDetails> customersFound = (List<PersonDetails>) client.toBlocking().retrieve(HttpRequest.GET(""), List.class);

        // Assert
        assertEquals(2, customersFound.size());
    }

    @Test
    public void whenDeletingTheCustomer_thenTheCustomerNoLongerExists() {
        // Arrange
        addCustomer(personDetails1);

        // Act
        String result = client.toBlocking().retrieve(HttpRequest.DELETE(personDetails1.getHexId()));

        // Assert
        assertEquals("{\"deletedCount\":1}", result);
    }

    @Test
    public void whenUpdatingACustomer_thenTheCustomerIsUpdated() {
        // Arrange
        addCustomer(personDetails1);
        PersonDetails
            updated = new PersonDetails(
            personDetails1.getId(), "Jane", "Doe");

        // Act
        String result = client.toBlocking().retrieve(HttpRequest.PUT(updated.getHexId(), updated));

        // Assert
        assertEquals("{\"matchedCount\":1,\"modifiedCount\":1,\"modifiedCountAvailable\":true}", result);
    }

    @Test
    public void whenCreatingACustomer_thenTheCustomerExists() {
        // Arrange

        // Act
        PersonDetails
            personDetails = client.toBlocking().retrieve(HttpRequest.POST("", "{\"firstName\":\"John\", \"lastName\":\"Doe\"}"), PersonDetails.class);

        // Assert
        assertEquals(personDetails.getFirstName(), "John");
        assertEquals(personDetails.getLastName(), "Doe");
        assertNotNull(personDetails.getHexId());
    }

    // Given no customers exist.

    @Test
    public void whenQueryingACustomer_thenQueryShouldReturnA404() {
        // Arrange

        // Act
        HttpClientResponseException e = Assertions.assertThrows(HttpClientResponseException.class, () -> {
                PersonDetails personDetails =
                    client.toBlocking().retrieve(HttpRequest.GET("/" + ObjectId.get()), PersonDetails.class);
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
        String result = client.toBlocking().retrieve(HttpRequest.PUT("/" + ObjectId.get(),
            personDetails1));

        // Assert
        assertEquals("{\"matchedCount\":0,\"modifiedCount\":0,\"modifiedCountAvailable\":true}", result);
    }
}
