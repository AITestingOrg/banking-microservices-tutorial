package com.ultimatesoftware.banking.customer.query.unit;

import com.ultimatesoftware.banking.customer.query.domain.controllers.CustomerController;
import com.ultimatesoftware.banking.customer.query.domain.models.Customer;
import com.ultimatesoftware.banking.customer.query.service.repositories.CustomerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerUnitTests {

    @InjectMocks
    private CustomerController customerController;
    @Mock
    private CustomerRepository customerRepository;

    private final String id = "TEST_ID";
    private final Customer customer = new Customer(id, "FirstName", "LastName");

    @Test
    public void onGetAllCustomers_callsRepositoryFindAll() {
        //act
        customerController.getCustomers();

        //assert
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void onGetCustomers_callsRepositoryFindByIdWithId() {
        //act
        customerController.getCustomer(id);

        //assert
        verify(customerRepository, times(1)).findOne(id);
    }

    @Test
    public void onGetCustomers_whenIdDoesNotExist_returnsNotFound() {
        //act
        ResponseEntity response = customerController.getCustomer(id);

        //assert
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void onCreateCustomers_repositorySaveIsCalled() {
        //act
        customerController.createCustomer(customer);

        //assert
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void onUpdateCustomers_repositoryFindOneIsCalled() {
         //act
        customerController.updateCustomer(id, customer);

        //assert
        verify(customerRepository, times(1)).findOne(id);
    }

    @Test
    public void onUpdateCustomersWithExistingId_repositorySaveIsCalled() {
        //arrange
        when(customerRepository.findOne(id)).thenReturn(customer);

        //act
        customerController.updateCustomer(id, customer);

        //assert
        verify(customerRepository, times(1)).save(customer);
    }


    @Test
    public void onUpdateCustomersWithNonExistingId_returnNotFound() {
        //act
        ResponseEntity response = customerController.updateCustomer(id, customer);

        //assert
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void onDeleteCustomers_repositoryFindOneIsCalled() {
        //act
        customerController.deleteCustomers(id);

        //assert
        verify(customerRepository, times(1)).findOne(id);
    }

    @Test
    public void onDeleteCustomersWithExistingId_repositoryDeleteIsCalled() {
        //arrange
        when(customerRepository.findOne(id)).thenReturn(customer);

        //act
        customerController.deleteCustomers(id);

        //assert
        verify(customerRepository, times(1)).delete(id);
    }


    @Test
    public void onDeleteCustomersWithNonExistingId_returnNotFound() {
        //act
        ResponseEntity response = customerController.deleteCustomers(id);

        //assert
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
