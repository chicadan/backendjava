package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName, lastName,email;
    private Set<AccountDTO> accounts;

    public ClientDTO(Client client){


        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccounts().stream().map(element -> new AccountDTO(element)).collect(Collectors.toSet());


    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccount() {
        return accounts;
    }
}

