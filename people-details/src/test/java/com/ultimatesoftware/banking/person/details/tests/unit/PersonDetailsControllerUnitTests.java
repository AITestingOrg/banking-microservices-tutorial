package com.ultimatesoftware.banking.person.details.tests.unit;

import com.ultimatesoftware.banking.api.repository.MongoRepository;

import com.ultimatesoftware.banking.people.details.controllers.PersonDetailsController;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
import io.reactivex.Maybe;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsControllerUnitTests {
    @InjectMocks
    private PersonDetailsController personDetailsController;

    @Mock
    private MongoRepository<PersonDetails> customerRepository;

    private final ObjectId id = ObjectId.get();
    private final PersonDetails
        personDetails = new PersonDetails(id, "FirstName", "LastName");

    @Test
    public void onGetAllCustomers_callsRepositoryFindAll() {
        //act
        personDetailsController.getAll();

        //assert
        verify(customerRepository, times(1)).findMany();
    }

    @Test
    public void onGetCustomers_callsRepositoryFindByIdWithId() {
        //act
        personDetailsController.get(id.toHexString());

        //assert
        verify(customerRepository, times(1)).findOne(id.toHexString());
    }

    @Test
    public void onGetCustomers_whenIdDoesNotExist_returnsNotFound() {
        // arrange
        when(customerRepository.findOne(id.toHexString())).thenReturn(Maybe.never());

        //act
        Maybe<PersonDetails> customer = personDetailsController
            .get(id.toHexString());

        //assert
        customer.test().assertEmpty();
    }
}
