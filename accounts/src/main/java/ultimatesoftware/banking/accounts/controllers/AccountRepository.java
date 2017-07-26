package ultimatesoftware.banking.accounts.controllers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ultimatesoftware.banking.accounts.models.Account;

import java.util.List;

@EnableMongoRepositories
public interface AccountRepository extends MongoRepository<Account, String> {

    public Account findByFirstName(String firstName);
    public List<Account> findByLastName(String lastName);
    Account findById(String id);
}
