package com.ultimatesoftware.banking.account.query.service.repositories;

import com.ultimatesoftware.banking.account.query.domain.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, UUID> {}
