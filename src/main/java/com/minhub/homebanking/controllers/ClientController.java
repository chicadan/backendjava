package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.RoleType;
import com.minhub.homebanking.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class ClientController {

    //PROPERTY
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public List<ClientDTO> getClients(){
        List<Client> listClient = clientRepository.findAll();
        return listClient.stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return (clientRepository.findById(id)
                .map(ClientDTO::new)
                .orElse(null));
    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
                @RequestParam String firstName, @RequestParam String lastName,
                @RequestParam String email, @RequestParam String password) {

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }
            if (clientRepository.findByEmail(email) !=  null) {
                return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
            }
            clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password),RoleType.CLIENT));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    @RequestMapping(value = "/clients/current", method = RequestMethod.GET)
    public ClientDTO getClientCurrent(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

;}

