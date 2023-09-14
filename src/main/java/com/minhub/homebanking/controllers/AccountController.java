package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.RoleType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.minhub.homebanking.utils.RandomUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.minhub.homebanking.utils.RandomUtils.generateRandomAccountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<List<AccountDTO>> getClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Account> clientAccounts = client.getAccounts();
        List<AccountDTO> accountDTOs = clientAccounts.stream().map(AccountDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(accountDTOs, HttpStatus.OK);
    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getRole() != RoleType.CLIENT) {
            return new ResponseEntity<>("Don't have permission to create account", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size() == 3) {
            return new ResponseEntity<>("Don't have permission to create another account", HttpStatus.FORBIDDEN);
        }

        // CREATE NEW ACCOUNT UNIQUE
        String accountNumber;
        Account newAccount = new Account();
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        newAccount.setClient(client);

        do {
            accountNumber = RandomUtils.generateRandomAccountNumber();
            newAccount.setNumber(accountNumber);
        } while (accountRepository.existsByNumber(accountNumber));

        // SAVE

        accountRepository.save(newAccount);
        client.addAccount(newAccount);

        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }


}