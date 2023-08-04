package com.minhub.homebanking;


import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new Client("Jack", "Bauer", "jb@mail.com"));
            repository.save(new Client("Chloe", "O'Brian","chob@mail.com"));
            repository.save(new Client("Kim", "Bauer","kimb@gmail.com"));

        };
    }
};





