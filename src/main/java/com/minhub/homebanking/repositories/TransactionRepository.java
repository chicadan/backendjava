package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}



