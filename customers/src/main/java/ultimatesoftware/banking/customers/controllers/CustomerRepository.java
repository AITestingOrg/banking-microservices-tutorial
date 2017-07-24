package ultimatesoftware.banking.customers.controllers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ultimatesoftware.banking.customers.models.Customer;

import java.util.List;

@EnableMongoRepositories
public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
    Customer findById(String id);
}