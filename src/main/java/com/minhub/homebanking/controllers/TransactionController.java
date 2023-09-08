package com.minhub.homebanking.controllers;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            Authentication authentication){

        // 403 forbidden, si el monto o la descripción estan vacios OK
        // 403 forbidden, si alguno de los numeros de cuenta estan vacios OK
        // 403 forbidden, si la cuenta de origen no existe OK
        // 403 forbidden, si la cuenta de destino no existe OK
        //403 forbidden, si la cuenta de origen no pertenece al cliente autenticado OK
        //403 forbidden, si el cliente no tiene fondos OK
        // 403 forbidden, si la cuenta de origen es la misma que la destino OK




        Client client = clientRepository.findByEmail(authentication.getName());
        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);

        // CHECK CLIENT AUTH


        // CHECK REQUEST PARAM EMPTY
        if(Double.isNaN(amount)|| description.isBlank()|| fromAccountNumber.isBlank()|| toAccountNumber.isBlank()){
            return new ResponseEntity<>("All data is required", HttpStatus.BAD_REQUEST);
        }
        // CHECK ACCOUNTS NUMBER BE DIFFERENT
        if(fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("Source and Target accounts cannot be the same",HttpStatus.BAD_REQUEST);
        }

        //CHECK ACCOUNT TO
        if(fromAccount==null ){
            return new ResponseEntity<>("Account not found",HttpStatus.FORBIDDEN);
        }

        // CHECK CLIENT - ACCOUNT TO
        Client authClient = fromAccount.getClient();
        String authUserName = authentication.getName();

        if(!authClient.getEmail().equals(authUserName)){
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);

        }
        // CHECK AMOUNT ACCOUNT TO
        if(fromAccount.getBalance()< amount){
            return new ResponseEntity<>("Insufficient balance",HttpStatus.BAD_REQUEST);

        }

        //CREATE TRANSFER
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount,description + "  TRANSFER TO: " + fromAccountNumber, LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description + "  TRANSFER FROM: " + toAccountNumber, LocalDateTime.now());


        //MAPPING TRANSFER-ACCOUNTS
        fromAccount.addTransaction(debitTransaction);
        toAccount.addTransaction(creditTransaction);

        //UPDATE BALANCE
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        //SAVE
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);


        return new ResponseEntity<>("Transaction created",HttpStatus.CREATED);//201
    }

}

