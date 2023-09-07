package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan,Long> {
    boolean existsById (Long id);

}
