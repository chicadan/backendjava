package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.CardDTO;
import com.minhub.homebanking.models.Card;
import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.models.CardType;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.CardColor;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getCards(){
        List<Card> listCard = cardRepository.findAll();
        return listCard.stream().map(CardDTO::new).collect(Collectors.toList());
    }
    @RequestMapping("/cards/{id}")
    public CardDTO getCardById (@PathVariable Long id){
        return new CardDTO(cardRepository.findById(id).orElse(null));
    }


    @GetMapping("/clients/current/cards")
    public ResponseEntity<List<CardDTO>> getClientCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Card> clientCards = cardRepository.findByClient(client);
        List<CardDTO> cardDTOs = clientCards.stream().map(CardDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }



    @PostMapping("clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor, Authentication authentication) {

        // CLIENT AUTH
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        //CHECK COLOR AND TYPE CARD ALLOW
        List<Card> cardUniqueTypeColor = client.getCards().stream()
                .filter(card -> card.getType() == cardType && card.getColor() == cardColor).collect(Collectors.toList());

        if ((long) cardUniqueTypeColor.size() ==1){
            return new ResponseEntity<>("It's not Allow", HttpStatus.FORBIDDEN);
        }

        // GENERATE CARD NUMBER RANDOM
        String cardNumber;

        do {
            cardNumber = RandomUtils.generateRandomCardNumber();
        } while (cardRepository.existsByNumber(cardNumber));

        // GENERATE SECURITY CODE RANDOM
        Integer cvv = RandomUtils.generateRandomCvv();


        // CREATE CARD
        Card newCard = new Card(cardType, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5), client.getFirstName()+ " " +client.getLastName(), cardColor);
        newCard.setClient(client);

        //ADD CARD
        client.addCard(newCard);
        // SAVE CARD
        cardRepository.save(newCard);

        return ResponseEntity.status(HttpStatus.CREATED).body("Card created successfully");  // 201
    }


}




