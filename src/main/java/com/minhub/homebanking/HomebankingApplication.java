package com.minhub.homebanking;


import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
        return (args) -> {
            //CREAR CLIENTE
            Client client1 = new Client("Melba", "Morel", "memo@mail.com");
            Client client2 = new Client("Alvaro", "Gonzalez", "agon@mail.com");

            //GUARDA BBDD CLIENTE
            clientRepository.save(client1);
            clientRepository.save(client2);

            //CREAR CUENTA

            Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);


            Account account2 = new Account("VIN002",LocalDate.now().plusDays(1), 7500.0);


            Account account3 = new Account("VIN003",LocalDate.now(), 2000.0);


            Account account4 = new Account("VIN004", LocalDate.now().plusDays(2), 10000.0);



            //AGREGAR CUENTA AL CLIENTE
            client1.addAccount(account1);

            client1.addAccount(account2);

            client2.addAccount(account3);

            client1.addAccount(account4);




            //SAVE
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);

            clientRepository.save(client1);
            clientRepository.save(client2);
        };
    }
};








