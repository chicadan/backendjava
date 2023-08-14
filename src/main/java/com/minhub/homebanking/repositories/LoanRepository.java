package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
