package com.minhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName, lastName,email;

    //1-N CLIENT-ACCOUNT
    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    //1-N CLIENT-CREDIT
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();


    public List<Loan> getLoans() {return loans.stream().map(element ->element.getLoan()).collect(Collectors.toList());
    }


    public Client() {
    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }



    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<ClientLoan> getClientLoans() {
       return loans;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
   }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        loans.add(clientLoan);
    }


}


