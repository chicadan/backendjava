package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
