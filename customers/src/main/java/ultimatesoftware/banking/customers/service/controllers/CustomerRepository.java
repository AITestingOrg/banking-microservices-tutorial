package ultimatesoftware.banking.customers.service.controllers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

import java.util.List;

@EnableMongoRepositories
public interface CustomerRepository extends MongoRepository<CustomerAggregate, String> {

    public CustomerAggregate findByFirstName(String firstName);
    public List<CustomerAggregate> findByLastName(String lastName);
    CustomerAggregate findById(String id);
}