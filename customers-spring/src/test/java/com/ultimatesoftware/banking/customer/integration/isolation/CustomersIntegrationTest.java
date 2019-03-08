package com.ultimatesoftware.banking.customer.integration.isolation;

import com.ultimatesoftware.banking.customer.CustomerApplication;
import com.ultimatesoftware.banking.customer.domain.models.Customer;
import com.ultimatesoftware.banking.customer.service.controllers.CustomerController;
import com.ultimatesoftware.banking.customer.service.repositories.CustomerRepository;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = CustomerApplication.class)
public class CustomersIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;


    private final String id1 = "12093be5-03a7-43a1-a892-a3f614bc6564";
    private final String id2 = "13147684-7d55-476c-ae11-8383407a7f13";
    private final Customer customer = new Customer(id1, "FirstName1", "LastName1");
    private final Customer customer2 = new Customer(id2, "FirstName2", "LastName2");


    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        customerRepository.save(customer);
        customerRepository.save(customer2);
    }

    @AfterEach
    public void teardown() {
        customerRepository.deleteAll();
    }

    // Given Customers Exist
    @Test
    public void whenQueryingACustomer_thenReturnThatCustomer() {
        // Arrange

        // Act
        Customer customerFound = customerController.getCustomer(id1).getBody();

        // Assert
        assertEquals(customer.getFirstName(), customerFound.getFirstName());
        assertEquals(customer.getLastName(), customerFound.getLastName());
    }

    @Test
    public void whenQueryingAllCustomers_thenReturnAllCustomers() {
        // Arrange

        // Act
        List<Customer> customersFound = customerController.getCustomers();

        // Assert
        assertEquals(2, customersFound.size());
    }

    @Test
    public void whenDeletingTheCustomer_thenTheCustomerNoLongerExists() {
        // Arrange

        // Act
        customerController.deleteCustomers(id1);

        // Assert
        Optional<Customer> customerFound = customerRepository.findOne(Example.of(customer));
        assertEquals(false, customerFound.isPresent());
    }

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
}
