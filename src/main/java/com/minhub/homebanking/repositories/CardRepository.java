package com.minhub.homebanking.repositories;

import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    int countByClientAndType(Client client, CardType cardtype);

    boolean existsByNumber(String number);

    List<Card> findByClient(Client client);
}
