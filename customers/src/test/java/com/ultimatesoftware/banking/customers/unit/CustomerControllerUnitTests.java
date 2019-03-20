package com.ultimatesoftware.banking.customers.unit;

import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.repository.MongoRepository;

import com.ultimatesoftware.banking.customers.controllers.CustomerController;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.reactivex.Maybe;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerUnitTests {
    @InjectMocks
    private CustomerController customerController;

    @Mock
    private MongoRepository<Customer> customerRepository;

    private final ObjectId id = ObjectId.get();
    private final Customer customer = new Customer(id, "FirstName", "LastName");

    @Test
    public void onGetAllCustomers_callsRepositoryFindAll() {
        //act
        customerController.getAll();

        //assert
        verify(customerRepository, times(1)).findMany();
    }

    @Test
    public void onGetCustomers_callsRepositoryFindByIdWithId() {
        //act
        customerController.get(id.toHexString());

        //assert
        verify(customerRepository, times(1)).findOne(id.toHexString());
    }

    @Test
    public void onGetCustomers_whenIdDoesNotExist_returnsNotFound() {
        // arrange
        when(customerRepository.findOne(id.toHexString())).thenReturn(Maybe.never());

        //act
        Maybe<Customer> customer = customerController.get(id.toHexString());

        //assert
        customer.test().assertEmpty();
    }

    @Test
    public void onCreateCustomers_repositorySaveIsCalled() {
        // arrange

        //act
        customerController.create(customer);

        //assert
        verify(customerRepository, times(1)).add(customer);
    }

    @Test
    public void onUpdateCustomersWithExistingId_repositorySaveIsCalled() {
        //arrange
        Maybe<UpdateResult> updateResultOr = Maybe.just(UpdateResult.acknowledged(1, 1L, null));
        when(customerRepository.replaceOne(id.toHexString(), customer)).thenReturn(updateResultOr);

        //act
        Maybe<UpdateResult> updateResult = customerController.update(id.toHexString(), customer);

        //assert
        verify(customerRepository, times(1)).replaceOne(id.toHexString(), customer);
        assertEquals(updateResult, updateResultOr);
    }

    @Test
    public void onDeleteCustomersWithExistingId_repositoryDeleteIsCalled() {
        //arrange

        //act
        customerController.delete(id.toHexString());

        //assert
        verify(customerRepository, times(1)).deleteOne(id.toHexString());
    }
}
