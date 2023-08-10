package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO extends Client {

    private Long id;
    private String firstName, lastName,email;
    private Set<AccountDTO> accounts;

    public ClientDTO(Client client){

        id = client.getId();

        firstName = client.getFirstName();

        lastName = client.getLastName();

        email = client.getEmail();

        accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());


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


    public Set<AccountDTO> getaccounts() {
        return accounts;
    }


}

