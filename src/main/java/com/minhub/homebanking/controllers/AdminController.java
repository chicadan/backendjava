/*package com.minhub.homebanking.controllers;

import com.minhub.homebanking.models.Client;
import com.minhub.homebanking.models.RoleType;
import com.minhub.homebanking.repositories.AccountRepository;
import com.minhub.homebanking.repositories.CardRepository;
import com.minhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;


    @PostMapping("/role")
    public ResponseEntity<Object> changeRole(@RequestParam Long id,
                                            @RequestParam String roleType){
        // CHECK NEW ROLE
        if (newRole == null || (newRole != RoleType.CLIENT && newRole != RoleType.ADMIN)) {
            return new ResponseEntity<>("Invalid role", HttpStatus.BAD_REQUEST);
        }

        // CHECK CLIENT
        Client client = clientRepository.findClientById(clientId);
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        // CHECK ROLE
        if (client.getRole() == newRole) {
            return new ResponseEntity<>("New role is the same as the current role", HttpStatus.BAD_REQUEST);
        }

        // CHANGE ROLE
        clientRepository.changeRole(clientId, newRole);

        return new ResponseEntity<>("Role changed successfully", HttpStatus.OK);
    }


}







}*/
