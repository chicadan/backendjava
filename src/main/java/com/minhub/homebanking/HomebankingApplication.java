package com.minhub.homebanking;


import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import com.minhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
        return (args) -> {
            //CREATE CLIENT
            Client client1 = new Client("Melba", "Morel", "memo@mail.com");
            Client client2 = new Client("Alvaro", "Gonzalez", "agon@mail.com");

            //SAVE BBDD CLIENT
            clientRepository.save(client1);
            clientRepository.save(client2);

            //CREATE ACCOUNT

            Account account1 = new Account("VIN001", LocalDate.now(), 5000.0,client1);
            account1.setClient(client1);

            Account account2 = new Account("VIN002",LocalDate.now().plusDays(1), 7500.0, client1);
            account2.setClient(client1);

            Account account3 = new Account("VIN003",LocalDate.now(), 2000.0, client2);
            account3.setNumber("VIN003");

            Account account4 = new Account("VIN004", LocalDate.now().plusDays(2), 10000.0, client2);
            account4.setNumber("VIN004");


            //ADD ACCOUNT TO CLIENT
            client1.addAccount(account1);

            client1.addAccount(account2);

            client2.addAccount(account3);

            client2.addAccount(account4);

            //SAVE BBDD
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);



            //CREATE TRANSACTIONS
            Transaction transaction1 = new Transaction(LocalDateTime.now(), 1000.0, account1, TransactionType.CREDIT);
            Transaction transaction2 = new Transaction(LocalDateTime.now(), -500.0, account1, TransactionType.DEBIT);
            Transaction transaction3 = new Transaction(LocalDateTime.now(), 2000.0, account2, TransactionType.CREDIT);
            Transaction transaction4 = new Transaction(LocalDateTime.now(), -1000.0, account2, TransactionType.DEBIT);

            // SAVE TRANSACTIONS
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);

            // ADD TRANSACTIONS TO ACCOUNT
            account1.addTransaction(transaction1);
            account1.addTransaction(transaction2);
            account2.addTransaction(transaction3);
            account2.addTransaction(transaction4);

            // UPDATE BBDD
            accountRepository.save(account1);
            accountRepository.save(account2);

        };
    }
};








