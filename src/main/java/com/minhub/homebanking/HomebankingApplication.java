package com.minhub.homebanking;


import com.minhub.homebanking.models.*;


import com.minhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;


@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
        return (args) -> {
            //CREATE CLIENT
            Client client1 = new Client("Melba", "Morel", "memo@mail.com");
            Client client2 = new Client("Alvaro", "Gonzalez", "agon@mail.com");

            //SAVE BBDD CLIENT
            clientRepository.save(client1);
            clientRepository.save(client2);

            //CREATE ACCOUNT
            Account account1 = new Account("VIN001",LocalDate.now(), 5000.0);
            Account account2 = new Account("VIN002",LocalDate.now().plusDays(1), 7500.0);
            Account account3 = new Account("VIN003",LocalDate.now(), 2000.0);
            Account account4 = new Account("VIN004",LocalDate.now().plusDays(2), 10000.0);


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
            Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000.0, "Transference",LocalDateTime.now());
            Transaction transaction2 = new Transaction(TransactionType.DEBIT, -500.0, "Transference", LocalDateTime.now());
            Transaction transaction3 = new Transaction(TransactionType.CREDIT, 2000.0, "Transference",LocalDateTime.now());
            Transaction transaction4 = new Transaction(TransactionType.DEBIT, -1000.0, "Transference",LocalDateTime.now());


            // ADD TRANSACTIONS TO ACCOUNT
            account1.addTransaction(transaction1);
            account2.addTransaction(transaction2);
            account3.addTransaction(transaction3);
            account4.addTransaction(transaction4);

            // SAVE TRANSACTIONS
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);

            // UPDATE BBDD
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);

            //CREATE LOAN
            Loan loan1 = new Loan("Mortgage Loan",500000.00, List.of(12, 24, 36, 48, 60));
            Loan loan2 = new Loan("Personal Loan", 100000.00, List.of(6,12,24));
            Loan loan3 = new Loan("Auto Loan", 300000.00, List.of(6,12,24,36));

            //SAVE LOAN
            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            //CREATE CLIENTLOAN
            ClientLoan clientLoan1 = new ClientLoan(400000.00, 60, client1, loan1);
            ClientLoan clientLoan2 = new ClientLoan(50000.00, 12, client1, loan2);
            ClientLoan clientLoan3 = new ClientLoan(100000.00, 24, client2, loan2);
            ClientLoan clientLoan4 = new ClientLoan(200000.00, 36, client2, loan3);

            //ADD  CLIENTLOAN TO CLIENT-LOAN
            client1.addClientLoan(clientLoan1);
            loan1.addClientLoan(clientLoan1);

            client1.addClientLoan(clientLoan2);
            loan2.addClientLoan(clientLoan2);

            client2.addClientLoan(clientLoan3);
            loan2.addClientLoan(clientLoan3);

            client2.addClientLoan(clientLoan4);
            loan3.addClientLoan(clientLoan4);


            //SAVE CLIENTLOAN
            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);

            //UPDATE CLIENT
            clientRepository.save(client1);
            clientRepository.save(client2);
        };
    }
};








