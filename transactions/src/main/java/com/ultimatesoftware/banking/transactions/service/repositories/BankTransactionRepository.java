package com.ultimatesoftware.banking.transactions.service.repositories;

import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface BankTransactionRepository extends MongoRepository<BankTransaction, String> {
    List<BankTransaction> findByCustomerId(UUID customerId);
}
