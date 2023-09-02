package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.RoleType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;

import com.minhub.homebanking.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class ClientController {

    //PROPERTY
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        List<Client> listClient = clientRepository.findAll();
        return listClient.stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity <Object> getClientId (@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO,HttpStatus.OK);
    }

    @RequestMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);//403
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);//403
        }

        Account account = null;
        do {
            String number = RandomUtils.generateRandomAccountNumber();
            account= new Account(number,LocalDate.now(),0.0);
        } while(accountRepository.existsByNumber(account.getNumber()));


        String number = RandomUtils.generateRandomCardNumber();
        Integer cvv = RandomUtils.generateRandomCvv();


        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password),RoleType.CLIENT);
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>("Registered", HttpStatus.CREATED);//201
    }

    //CLIENT AUTHENTICATION
    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    }